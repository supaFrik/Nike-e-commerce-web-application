package vn.demo.nike.features.checkout.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.demo.nike.features.catalog.product.domain.Product;
import vn.demo.nike.features.catalog.product.domain.ProductColor;
import vn.demo.nike.features.catalog.product.domain.ProductVariant;
import vn.demo.nike.features.catalog.product.repository.ProductVariantRepository;
import vn.demo.nike.features.identity.user.domain.Address;
import vn.demo.nike.features.identity.user.domain.User;
import vn.demo.nike.features.identity.user.repository.AddressRepository;
import vn.demo.nike.features.identity.user.repository.UserRepository;
import vn.demo.nike.features.identity.user.request.CurrentUserProvider;
import vn.demo.nike.features.cart.domain.CartItem;
import vn.demo.nike.features.cart.repository.CartItemRepository;
import vn.demo.nike.features.order.domain.Order;
import vn.demo.nike.features.order.domain.OrderItem;
import vn.demo.nike.features.order.domain.enums.ShippingMethod;
import vn.demo.nike.features.checkout.dto.CheckoutItemSnapshotDto;
import vn.demo.nike.features.checkout.dto.PlaceCheckoutRequest;
import vn.demo.nike.features.checkout.dto.CheckoutInitiationResponse;
import vn.demo.nike.features.checkout.exception.InvalidCheckoutRequestException;
import vn.demo.nike.features.checkout.exception.UnauthenticatedCheckoutException;
import vn.demo.nike.features.order.repository.OrderRepository;
import vn.demo.nike.features.order.domain.enums.OrderStatus;
import vn.demo.nike.features.payment.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;
    private final AddressRepository addressRepository;
    private final List<CheckoutPaymentHandler> checkoutPaymentHandlers;

    @Transactional
    public CheckoutInitiationResponse placeOrder(PlaceCheckoutRequest request) {
        validateRequest(request);

        User user = requireCurrentUser();
        ShippingMethod shippingMethod = resolveShippingMethod(request);
        Address shippingAddress = resolveShippingAddress(user, request);

        List<CartItem> cartItems = loadCartItems(user.getId());
        List<CheckoutItemSnapshotDto> snapshots = buildOrderItemSnapshots(cartItems);

        BigDecimal subtotal = calculateSubtotal(snapshots);

        PaymentMethod paymentMethod = resolvePaymentMethod(request);

        Order order = createOrder(user, shippingMethod, shippingAddress, subtotal, BigDecimal.ZERO, paymentMethod);

        attachOrderItems(order, snapshots);

        deductStock(cartItems);

        Order savedOrder = orderRepository.save(order);

        clearCart(cartItems);

        return handleCheckoutCompletion(savedOrder, snapshots);
    }

    private PaymentMethod resolvePaymentMethod(PlaceCheckoutRequest request) {
        if (request.getPaymentMethod() == null) {
            throw new InvalidCheckoutRequestException("Payment method is required");
        }

        try {
            return PaymentMethod.valueOf(request.getPaymentMethod().trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidCheckoutRequestException("Invalid payment method: " + request.getPaymentMethod());
        }
    }

    private CheckoutInitiationResponse handleCheckoutCompletion(Order order, List<CheckoutItemSnapshotDto> snapshots) {
        return checkoutPaymentHandlers.stream()
                .filter(handler -> handler.supports(order.getPaymentMethod()))
                .findFirst()
                .map(handler -> handler.handle(order, snapshots))
                .orElseGet(() -> buildPendingExternalPaymentResponse(order, snapshots));
    }

    private CheckoutInitiationResponse buildPendingExternalPaymentResponse(Order order, List<CheckoutItemSnapshotDto> snapshots) {
        int itemCount = snapshots.stream()
                .map(CheckoutItemSnapshotDto::getQuantity)
                .filter(quantity -> quantity != null)
                .mapToInt(Integer::intValue)
                .sum();

        return new CheckoutInitiationResponse(
                order.getId(),
                itemCount,
                order.getSubtotal(),
                order.getShippingCost(),
                order.getDiscount(),
                order.getTotal(),
                order.getOrderStatus(),
                order.getPaymentMethod(),
                true,
                null,
                null,
                snapshots
        );
    }

    private void validateRequest(PlaceCheckoutRequest request) {
        if (request == null) {
            throw new InvalidCheckoutRequestException("Request must not be null");
        }
        requireText(request.getRecipientName(), "Recipient name is required");
        requireText(request.getPhone(), "Phone is required");
        requireText(request.getLine1(), "Address line 1 is required");
        requireText(request.getCity(), "City is required");
        requireText(request.getCountry(), "Country is required");
    }

    private User requireCurrentUser() {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (currentUserId == null) {
            throw new UnauthenticatedCheckoutException();
        }
        return userRepository.findById(currentUserId).orElseThrow(UnauthenticatedCheckoutException::new);
    }

    private ShippingMethod resolveShippingMethod(PlaceCheckoutRequest request) {
        if (request.getShippingMethod() == null) {
            return ShippingMethod.STANDARD;
        }

        try {
            return ShippingMethod.valueOf(request.getShippingMethod().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidCheckoutRequestException(
                    "Invalid shipping method: " + request.getShippingMethod()
            );
        }
    }

    private Address resolveShippingAddress(User user, PlaceCheckoutRequest request) {
        Long addressId = request.getAddressId();
        Address address;

        if (addressId != null) {
            address = addressRepository
                    .findByIdAndUser_Id(addressId, user.getId())
                    .orElseThrow(() -> new InvalidCheckoutRequestException("Address does not belong to user"));
        } else {
            address = addressRepository
                    .findByUser_IdAndPrimaryAddressTrue(user.getId())
                    .orElseGet(() -> user.getAddress());
        }

        if (address == null) {
            address = new Address();
            address.setUser(user);
            address.setPrimaryAddress(user.getAddresses() == null || user.getAddresses().isEmpty());
        }

        applyShippingDetails(address, request);
        return addressRepository.save(address);
    }

    private List<CartItem> loadCartItems(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUser_Id(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new InvalidCheckoutRequestException("Cart is empty");
        }
        return cartItems;
    }

    private List<CheckoutItemSnapshotDto> buildOrderItemSnapshots(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new InvalidCheckoutRequestException("No cartItems found for user");
        }

        List<CheckoutItemSnapshotDto> orderItemSnapshots = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Long variantId = cartItem.getVariant().getId();

            ProductVariant variant = productVariantRepository.findByIdForUpdate(variantId)
                    .orElseThrow(() ->
                            new InvalidCheckoutRequestException("Variant not found: " + variantId)
                    );

            if (!variant.getActive() || variant.getStock() == null || variant.getStock() < cartItem.getQuantity()) {
                throw new InvalidCheckoutRequestException("Product is no longer available: " + variantId);
            }

            ProductColor color = variant.getColor();
            Product product = color.getProduct();
            BigDecimal unitPrice = resolveCurrentPrice(cartItem);
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            CheckoutItemSnapshotDto orderItemSnapshot = new CheckoutItemSnapshotDto(
                    product.getId(),
                    variant.getId(),
                    variant.getSku(),
                    product.getName(),
                    variant.getSize(),
                    color.getColorName(),
                    unitPrice,
                    cartItem.getQuantity(),
                    lineTotal
            );
            orderItemSnapshots.add(orderItemSnapshot);
        }
        return orderItemSnapshots;
    }

    private BigDecimal resolveCurrentPrice(CartItem cartItem) {
        ProductVariant variant = cartItem.getVariant();
        ProductColor color = variant.getColor();
        Product product = color.getProduct();

        return product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();
    }

    private void applyShippingDetails(Address address, PlaceCheckoutRequest request) {
        address.setRecipientName(trimToNull(request.getRecipientName()));
        address.setPhone(trimToNull(request.getPhone()));
        address.setLine1(trimToNull(request.getLine1()));
        address.setLine2(trimToNull(request.getLine2()));
        address.setCity(trimToNull(request.getCity()));
        address.setProvince(trimToNull(request.getProvince()));
        address.setPostalCode(trimToNull(request.getPostalCode()));
        address.setCountry(trimToNull(request.getCountry()));
    }

    private void requireText(String value, String message) {
        if (trimToNull(value) == null) {
            throw new InvalidCheckoutRequestException(message);
        }
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private BigDecimal calculateSubtotal(List<CheckoutItemSnapshotDto> snapshots) {
        if (snapshots == null || snapshots.isEmpty()) {
            throw new InvalidCheckoutRequestException("No cart items found for user");
        }

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CheckoutItemSnapshotDto snapshot : snapshots) {
            if (snapshot.getLineTotal() == null) {
                throw new InvalidCheckoutRequestException("Line total is null");
            }
            subtotal = subtotal.add(snapshot.getLineTotal());

        }
        return subtotal;
    }

    private Order createOrder(User user, ShippingMethod shippingMethod, Address shippingAddress, BigDecimal subtotal, BigDecimal discount, PaymentMethod paymentMethod) {
        if (user == null) {
            throw new InvalidCheckoutRequestException("User must not be null");
        }

        if (shippingMethod == null) {
            throw new InvalidCheckoutRequestException("Shipping method must not be null");
        }

        if (shippingAddress == null) {
            throw new InvalidCheckoutRequestException("Shipping address must not be null");
        }

        if (subtotal == null || subtotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidCheckoutRequestException("Invalid subtotal");
        }

        if (discount == null || discount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidCheckoutRequestException("Invalid discount");
        }

        BigDecimal shippingCost = shippingMethod.getCost() == null ? BigDecimal.ZERO : shippingMethod.getCost();

        BigDecimal total = subtotal.add(shippingCost).subtract(discount);

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidCheckoutRequestException("Total amount cannot be negative");
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingMethod(shippingMethod);
        order.setShippingRecipientName(shippingAddress.getRecipientName());
        order.setShippingPhone(shippingAddress.getPhone());
        order.setShippingLine1(shippingAddress.getLine1());
        order.setShippingLine2(shippingAddress.getLine2());
        order.setShippingCity(shippingAddress.getCity());
        order.setShippingProvince(shippingAddress.getProvince());
        order.setShippingPostalCode(shippingAddress.getPostalCode());
        order.setShippingCountry(shippingAddress.getCountry());
        order.setShippingCost(shippingCost);
        order.setDiscount(discount);
        order.setSubtotal(subtotal);
        order.setTotal(total);
        order.setPaymentMethod(paymentMethod);
        order.setOrderStatus(resolveInitialOrderStatus(paymentMethod));

        return order;
    }

    private OrderStatus resolveInitialOrderStatus(PaymentMethod paymentMethod) {
        if (paymentMethod == PaymentMethod.COD) {
            return OrderStatus.PROCESSING;
        }
        return OrderStatus.PENDING_PAYMENT;
    }

    private void attachOrderItems(Order order, List<CheckoutItemSnapshotDto> snapshots) {
        if (order == null || snapshots == null || snapshots.isEmpty()) {
            throw new InvalidCheckoutRequestException("No order items found for user");
        }

        if (order.getItems() == null) {
            order.setItems(new ArrayList<>());
        }

        for (CheckoutItemSnapshotDto snapshot : snapshots) {
            OrderItem orderItem = toOrderItem(order, snapshot);
            order.getItems().add(orderItem);
        }
    }

    private OrderItem toOrderItem(Order order, CheckoutItemSnapshotDto snapshot) {
        if (order == null || snapshot == null) {
            throw new InvalidCheckoutRequestException("Snapshot must not be null");
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductId(snapshot.getProductId());
        orderItem.setVariantId(snapshot.getVariantId());
        orderItem.setSku(snapshot.getSku());

        orderItem.setProductName(snapshot.getProductName());
        orderItem.setSize(snapshot.getSize());
        orderItem.setColor(snapshot.getColor());
        orderItem.setUnitPrice(snapshot.getUnitPrice());
        orderItem.setQuantity(snapshot.getQuantity());
        orderItem.setLineTotal(snapshot.getLineTotal());

        return orderItem;
    }

    private void deductStock(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            Long variantId = cartItem.getVariant().getId();
            ProductVariant variant = productVariantRepository.findByIdForUpdate(variantId)
                    .orElseThrow(() -> new InvalidCheckoutRequestException("Variant not found : " + variantId));

            if (!variant.getActive()) {
                throw new InvalidCheckoutRequestException("Variant not active: " + variantId);
            }

            Integer requestedQuantity = cartItem.getQuantity();
            Integer currentStock = variant.getStock();

            if (variant.getStock() == null || variant.getStock() < requestedQuantity) {
                throw new InvalidCheckoutRequestException("Insufficient stock: " + variantId);
            }

            Integer newStock = currentStock - requestedQuantity;
            variant.setStock(newStock);
            cartItem.setVariant(variant);
        }
    }

    private void clearCart(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new InvalidCheckoutRequestException("No cart items found for user");
        }

        cartItemRepository.deleteAll(cartItems);
    }
}
