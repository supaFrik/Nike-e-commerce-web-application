package vn.demo.nike.features.checkout.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.demo.nike.features.catalog.product.domain.Product;
import vn.demo.nike.features.catalog.product.domain.ProductColor;
import vn.demo.nike.features.catalog.product.domain.ProductImage;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;
import vn.demo.nike.features.catalog.cart.response.CartItemViewResponse;
import vn.demo.nike.features.catalog.cart.response.CartSummaryResponse;
import vn.demo.nike.features.catalog.cart.service.CartService;
import vn.demo.nike.features.order.domain.Order;
import vn.demo.nike.features.order.domain.OrderItem;
import vn.demo.nike.features.order.domain.enums.ShippingMethod;
import vn.demo.nike.features.checkout.dto.CheckoutPageViewData;
import vn.demo.nike.features.order.repository.OrderRepository;
import vn.demo.nike.shared.util.ProductImageUrlResolver;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckoutPageViewService {
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public CheckoutPageViewData buildCheckoutPage(Long userId, Long orderId) {
        CartSummaryResponse cart = cartService.getCurrentCart();
        Optional<Order> order = resolveCheckoutOrder(userId, orderId, cart.getItemCount() > 0);

        if (cart.getItemCount() == 0 && order.isPresent()) {
            cart = buildSummaryFromOrder(order.get());
        }

        Order checkoutOrder = order.orElse(null);
        return new CheckoutPageViewData(
                cart,
                checkoutOrder,
                checkoutOrder != null ? checkoutOrder.getShippingMethod() : ShippingMethod.STANDARD,
                cart.getItemCount() > 0,
                checkoutOrder != null
        );
    }

    private Optional<Order> resolveCheckoutOrder(Long userId, Long orderId, boolean hasCartItems) {
        if (orderId != null) {
            return orderRepository.findByIdAndUser_Id(orderId, userId);
        }
        if (hasCartItems) {
            return Optional.empty();
        }
        return orderRepository.findTopByUser_IdOrderByCreateDateDesc(userId);
    }

    private CartSummaryResponse buildSummaryFromOrder(Order order) {
        List<CartItemViewResponse> items = order.getItems().stream()
                .map(this::toCartItemView)
                .toList();

        int itemCount = items.stream()
                .map(CartItemViewResponse::getQuantity)
                .filter(quantity -> quantity != null)
                .mapToInt(Integer::intValue)
                .sum();

        BigDecimal subtotal = defaultAmount(order.getSubtotal());
        BigDecimal discount = defaultAmount(order.getDiscount());
        BigDecimal total = defaultAmount(order.getTotal()).subtract(defaultAmount(order.getShippingCost()));

        return new CartSummaryResponse(items, itemCount, subtotal, discount, total);
    }

    private CartItemViewResponse toCartItemView(OrderItem item) {
        Product product = productRepository.findById(item.getProductId()).orElse(null);

        return new CartItemViewResponse(
                null,
                item.getProductId(),
                item.getProductName(),
                product != null && product.getCategory() != null ? product.getCategory().getName() : null,
                resolveProductImage(product),
                item.getColor(),
                item.getSize(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getLineTotal(),
                null,
                true
        );
    }

    private BigDecimal defaultAmount(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String resolveProductImage(Product product) {
        if (product == null || product.getColors() == null || product.getColors().isEmpty()) {
            return null;
        }

        return product.getColors().stream()
                .flatMap(color -> safeImages(color).stream())
                .sorted(Comparator.comparing(
                        ProductImage::getOrderIndex,
                        Comparator.nullsLast(Integer::compareTo)
                ))
                .filter(image -> Boolean.TRUE.equals(image.getIsMainForColor()))
                .map(ProductImage::getPath)
                .map(ProductImageUrlResolver::toPublicUrl)
                .findFirst()
                .orElseGet(() -> product.getColors().stream()
                        .flatMap(color -> safeImages(color).stream())
                        .sorted(Comparator.comparing(
                                ProductImage::getOrderIndex,
                                Comparator.nullsLast(Integer::compareTo)
                        ))
                        .map(ProductImage::getPath)
                        .map(ProductImageUrlResolver::toPublicUrl)
                        .findFirst()
                        .orElse(null));
    }

    private List<ProductImage> safeImages(ProductColor color) {
        return color.getImages() == null ? List.of() : color.getImages();
    }
}
