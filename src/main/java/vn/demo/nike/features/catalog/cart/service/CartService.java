package vn.demo.nike.features.catalog.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.demo.nike.features.catalog.cart.exception.*;
import vn.demo.nike.features.catalog.product.domain.Product;
import vn.demo.nike.features.catalog.product.domain.ProductColor;
import vn.demo.nike.features.catalog.product.domain.ProductImage;
import vn.demo.nike.features.catalog.product.domain.ProductVariant;
import vn.demo.nike.features.catalog.product.repository.ProductVariantRepository;
import vn.demo.nike.features.identity.user.domain.User;
import vn.demo.nike.features.identity.user.repository.UserRepository;
import vn.demo.nike.features.identity.user.request.CurrentUserProvider;
import vn.demo.nike.features.catalog.cart.domain.CartItem;
import vn.demo.nike.features.catalog.cart.repository.CartItemRepository;
import vn.demo.nike.features.catalog.cart.request.AddToCartRequest;
import vn.demo.nike.features.catalog.cart.request.UpdateCartItemQuantityRequest;
import vn.demo.nike.features.catalog.cart.response.AddToCartResponse;
import vn.demo.nike.features.catalog.cart.response.CartCountResponse;
import vn.demo.nike.features.catalog.cart.response.CartItemViewResponse;
import vn.demo.nike.features.catalog.cart.response.CartSummaryResponse;
import vn.demo.nike.shared.util.ProductImageUrlResolver;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CurrentUserProvider currentUserProvider;
    private final UserRepository userRepository;

    @Transactional
    public AddToCartResponse addToCart(AddToCartRequest request) {
        validateAddToCartRequest(request);

        Long userId = requireCurrentUserId();
        User user = getCurrentUser(userId);
        ProductVariant variant = getVariantOrThrow(request.getVariantId());
        ensureVariantCanBePurchased(variant);

        CartItem cartItem = cartItemRepository.findByUser_IdAndVariant_Id(userId, request.getVariantId()).orElse(null);
        if (cartItem != null) {
            int newQty = request.getQuantity() + cartItem.getQuantity();
            ensureRequestedQuantityWithinStock(newQty, variant);
            cartItem.setQuantity(newQty);
        } else {
            ensureRequestedQuantityWithinStock(request.getQuantity(), variant);
            cartItem = new CartItem(user, variant, request.getQuantity());
        }

        cartItemRepository.save(cartItem);

        int itemCount = cartItemRepository.sumQuantityByUser_Id(userId);

        return new AddToCartResponse(
                true,
                "Added to cart successfully",
                itemCount
        );
    }

    @Transactional(readOnly = true)
    public CartSummaryResponse getCurrentCart() {
        Long userId = requireCurrentUserId();
        User user = getCurrentUser(userId);
        return buildCartSummary(user);
    }

    @Transactional
    public CartSummaryResponse updateCartItemQuantity(Long cartItemId, UpdateCartItemQuantityRequest request) {
        if(request == null || request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new InvalidCartQuantityException();
        }

        Long currentUserId = requireCurrentUserId();
        User user = getCurrentUser(currentUserId);
        CartItem cartItem = cartItemRepository.findByIdAndUser_Id(cartItemId, currentUserId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        ProductVariant variant = cartItem.getVariant();
        ensureVariantCanBePurchased(variant);
        ensureRequestedQuantityWithinStock(request.getQuantity(), variant);
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
        return buildCartSummary(user);
    }

    @Transactional
    public CartSummaryResponse removeCartItem(Long cartItemId) {
        Long userId = requireCurrentUserId();
        User user = getCurrentUser(userId);
        CartItem cartItem = cartItemRepository.findByIdAndUser_Id(cartItemId, userId).orElseThrow(() -> new CartItemNotFoundException(cartItemId));
        cartItemRepository.delete(cartItem);
        return buildCartSummary(user);
    }

    @Transactional(readOnly = true)
    public CartCountResponse getCartCount() {
        int itemCount =  cartItemRepository.sumQuantityByUser_Id(requireCurrentUserId());
        return new CartCountResponse(
                itemCount
        );
    }

    private void validateAddToCartRequest(AddToCartRequest request) {
        if (request == null || request.getVariantId() == null || request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new InvalidCartQuantityException();
        }
    }


    private Long requireCurrentUserId() {
        Long userId = currentUserProvider.getCurrentUserId();
        if (userId == null) {
            throw new UnauthenticatedUserException();
        }
        return userId;
    }

    private User getCurrentUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UnauthenticatedUserException::new);
    }

    private ProductVariant getVariantOrThrow(Long variantId) {
        return productVariantRepository.findById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));
    }

    private void ensureVariantCanBePurchased(ProductVariant variant) {
        if (!Boolean.TRUE.equals(variant.getActive())) {
            throw new InactiveVariantException(variant.getId());
        }
        if (variant.getStock() == null || variant.getStock() <= 0) {
            throw new InsufficientStockException("Variant is out of stock");
        }
    }

    private void ensureRequestedQuantityWithinStock(Integer quantity, ProductVariant variant) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidCartQuantityException();
        }
        if (variant.getStock() == null || quantity > variant.getStock()) {
            throw new InsufficientStockException("Requested quantity exceeds available stock");
        }
    }

    private CartSummaryResponse buildCartSummary(User user) {
        List<CartItemViewResponse> items = cartItemRepository.findByUser_Id(user.getId()).stream()
                .map(this::mapCartItem)
                .toList();

        int itemsCount = items.stream()
                .map(CartItemViewResponse::getQuantity)
                .filter(quantity -> quantity != null)
                .mapToInt(Integer::intValue)
                .sum();
        BigDecimal subTotal = calculateSubtotal(items);
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal total = subTotal.subtract(discount);

        return new CartSummaryResponse(
                items, itemsCount, subTotal, discount, total
        );
    }

    private CartItemViewResponse mapCartItem(CartItem cartItem) {
        ProductVariant variant = cartItem.getVariant();
        ProductColor color = variant.getColor();
        Product product = color.getProduct();

        BigDecimal unitPrice = resolveUnitPrice(cartItem);
        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        String categoryName = product.getCategory().getName() != null ? product.getCategory().getName() : null;
        return new CartItemViewResponse(
                cartItem.getId(),
                product.getId(),
                product.getName(),
                categoryName,
                resolveCartImage(cartItem),
                color.getColorName(),
                variant.getSize(),
                unitPrice,
                cartItem.getQuantity(),
                lineTotal,
                variant.getStock(),
                variant.getActive()
        );
    }

    private String resolveCartImage(CartItem cartItem) {
        ProductColor color = cartItem.getVariant().getColor();
        List<ProductImage> images = color.getImages();

        if(images == null || images.isEmpty()) {
            return null;
        }

        return images.stream()
                .sorted(Comparator.comparing(
                        ProductImage::getOrderIndex,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ))
                .filter(image -> Boolean.TRUE.equals(image.getIsMainForColor()))
                .map(ProductImage::getPath)
                .map(ProductImageUrlResolver::toPublicUrl)
                .findFirst()
                .orElseGet(() -> images.stream()
                        .sorted(Comparator.comparing(
                                ProductImage::getOrderIndex,
                                Comparator.nullsLast(Integer::compareTo)
                        ))
                        .map(ProductImage::getPath)
                        .map(ProductImageUrlResolver::toPublicUrl)
                        .findFirst()
                        .orElse(null));
    }

    private BigDecimal resolveUnitPrice(CartItem cartItem) {
        Product product = cartItem.getVariant().getColor().getProduct();
        if(product.getSalePrice() != null) {
            return product.getSalePrice();
        }
        return product.getPrice();
    }

    private BigDecimal calculateSubtotal(List<CartItemViewResponse> items) {
        return items.stream()
                .map(CartItemViewResponse::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
