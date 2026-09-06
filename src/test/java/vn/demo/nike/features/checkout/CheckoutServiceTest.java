package vn.demo.nike.features.checkout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.demo.nike.features.catalog.cart.entity.CartItem;
import vn.demo.nike.features.catalog.cart.repository.CartItemRepository;
import vn.demo.nike.features.catalog.product.entity.Product;
import vn.demo.nike.features.catalog.product.entity.ProductColor;
import vn.demo.nike.features.catalog.product.entity.ProductVariant;
import vn.demo.nike.features.catalog.product.repository.ProductVariantRepository;
import vn.demo.nike.features.checkout.dto.request.PlaceCheckoutRequest;
import vn.demo.nike.features.checkout.dto.response.CheckoutInitiationResponse;
import vn.demo.nike.features.checkout.exception.InvalidCheckoutRequestException;
import vn.demo.nike.features.checkout.exception.UnauthenticatedCheckoutException;
import vn.demo.nike.features.checkout.service.CheckoutService;
import vn.demo.nike.features.order.entity.Order;
import vn.demo.nike.features.order.enums.OrderStatus;
import vn.demo.nike.features.order.enums.ShippingMethod;
import vn.demo.nike.features.order.repository.OrderRepository;
import vn.demo.nike.features.user.entity.Address;
import vn.demo.nike.features.user.entity.User;
import vn.demo.nike.features.user.repository.AddressRepository;
import vn.demo.nike.features.user.repository.UserRepository;
import vn.demo.nike.features.user.request.CurrentUserProvider;
import vn.demo.nike.infras.payment.vnpay.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceTest {
    @Mock private CartItemRepository cartItemRepository;
    @Mock private ProductVariantRepository productVariantRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private CurrentUserProvider currentUserProvider;
    @Mock private AddressRepository addressRepository;
    @InjectMocks private CheckoutService checkoutService;

    private PlaceCheckoutRequest createValidRequest() {
        PlaceCheckoutRequest r = new PlaceCheckoutRequest();
        r.setRecipientName("John Doe");
        r.setPhone("0123456789");
        r.setLine1("123 Nguyen Trai");
        r.setCity("Hanoi");
        r.setCountry("Vietnam");
        r.setPaymentMethod("COD");
        r.setShippingMethod("STANDARD");
        return r;
    }

    private User userWithId(long id) {
        User u = new User();
        u.setId(id);
        u.setAddresses(new ArrayList<>());
        return u;
    }

    private Address savedAddress(long userId) {
        User u = userWithId(userId);
        Address a = new Address();
        a.setId(10L);
        a.setUser(u);
        a.setRecipientName("John Doe");
        a.setPhone("0123456789");
        a.setLine1("123 Nguyen Trai");
        a.setCity("Hanoi");
        a.setCountry("Vietnam");
        a.setPrimaryAddress(true);
        return a;
    }

    private CartItem cartItemWithVariant(User user, long variantId, int qty, Integer stock, Boolean active, BigDecimal price, BigDecimal salePrice) {
        Product product = new Product();
        product.setId(1L);
        product.setName("Air Max");
        product.setPrice(price);
        product.setSalePrice(salePrice);
        ProductColor color = new ProductColor();
        color.setId(1L);
        color.setColorName("Red");
        color.setProduct(product);
        ProductVariant variant = new ProductVariant();
        variant.setId(variantId);
        variant.setSku("SKU-" + variantId);
        variant.setSize("M");
        variant.setActive(active);
        variant.setStock(stock);
        variant.setColor(color);
        CartItem ci = new CartItem();
        ci.setId(1L);
        ci.setUser(user);
        ci.setVariant(variant);
        ci.setQuantity(qty);
        return ci;
    }

    private void stubAuthAndAddress(long userId) {
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        lenient().when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    // ---- 1..26 ----

    @Test void shouldRejectNullRequest() {
        assertThrows(InvalidCheckoutRequestException.class, () -> checkoutService.placeOrder(null));
    }

    @Test void shouldRejectUnauthenticatedUser() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(null);
        assertThrows(UnauthenticatedCheckoutException.class, () -> checkoutService.placeOrder(createValidRequest()));
    }

    @Test void shouldRejectUnauthenticatedUserWhenCurrentUserDoesNotExist() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findWithAddressesById(1L)).thenReturn(Optional.empty());
        assertThrows(UnauthenticatedCheckoutException.class, () -> checkoutService.placeOrder(createValidRequest()));
    }

    @Test void shouldRejectEmptyCart() {
        stubAuthAndAddress(1L);
        when(cartItemRepository.findByUser_Id(1L)).thenReturn(List.of());
        var ex = assertThrows(InvalidCheckoutRequestException.class, () -> checkoutService.placeOrder(createValidRequest()));
        assertEquals("Cart is empty", ex.getMessage());
    }

    @Test void shouldRejectMissingRecipientName() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setRecipientName(null);
        stubAuthAndAddress(1L);
        var ex = assertThrows(InvalidCheckoutRequestException.class, () -> checkoutService.placeOrder(req));
        assertEquals("Recipient name is required", ex.getMessage());
    }

    @Test void shouldRejectMissingPhone() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setPhone("  ");
        stubAuthAndAddress(1L);
        var ex = assertThrows(InvalidCheckoutRequestException.class, () -> checkoutService.placeOrder(req));
        assertEquals("Phone is required", ex.getMessage());
    }

    @Test void shouldRejectMissingAddress() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setLine1(null);
        stubAuthAndAddress(1L);
        var ex = assertThrows(InvalidCheckoutRequestException.class, () -> checkoutService.placeOrder(req));
        assertEquals("Address is required", ex.getMessage());
    }

    @Test void shouldRejectMissingCity() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setCity("");
        stubAuthAndAddress(1L);
        var ex = assertThrows(InvalidCheckoutRequestException.class, () -> checkoutService.placeOrder(req));
        assertEquals("City is required", ex.getMessage());
    }

    @Test void shouldRejectMissingCountry() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setCountry(null);
        stubAuthAndAddress(1L);
        var ex = assertThrows(InvalidCheckoutRequestException.class, () -> checkoutService.placeOrder(req));
        assertEquals("Country is required", ex.getMessage());
    }

    @Test void shouldUseStandardShippingWhenNotProvided() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setShippingMethod(null);
        // full happy path but shipping null -> should default STANDARD and succeed
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        CartItem ci = cartItemWithVariant(u, variantId, 2, 10, true, BigDecimal.valueOf(100), null);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.of(ci.getVariant()));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> { Order o = inv.getArgument(0); o.setId(100L); return o; });

        CheckoutInitiationResponse res = checkoutService.placeOrder(req);

        ArgumentCaptor<Order> cap = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(cap.capture());
        assertEquals(ShippingMethod.STANDARD, cap.getValue().getShippingMethod());
        assertNotNull(res);
    }

    @Test void shouldUseStandardShippingWhenBlankProvided() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setShippingMethod("  ");
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        CartItem ci = cartItemWithVariant(u, variantId, 1, 10, true, BigDecimal.valueOf(100), null);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.of(ci.getVariant()));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> { Order o = inv.getArgument(0); o.setId(100L); return o; });

        assertDoesNotThrow(() -> checkoutService.placeOrder(req));
        ArgumentCaptor<Order> cap = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(cap.capture());
        assertEquals(ShippingMethod.STANDARD, cap.getValue().getShippingMethod());
    }

    @Test void shouldRejectInvalidShippingMethod() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setShippingMethod("INVALID_METHOD");
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        User u = userWithId(1L);
        when(userRepository.findWithAddressesById(1L)).thenReturn(Optional.of(u));
        var ex = assertThrows(InvalidCheckoutRequestException.class, () -> checkoutService.placeOrder(req));
        assertTrue(ex.getMessage().contains("Invalid shipping method"));
    }

    @Test void shouldRejectMissingPaymentMethod() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setPaymentMethod(null);
        // need up to subtotal before payment check
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        CartItem ci = cartItemWithVariant(u, variantId, 1, 10, true, BigDecimal.valueOf(100), null);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.of(ci.getVariant()));

        var ex = assertThrows(InvalidCheckoutRequestException.class, () -> checkoutService.placeOrder(req));
        assertEquals("Payment method is required", ex.getMessage());
    }

    @Test void shouldRejectInvalidPaymentMethod() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setPaymentMethod("BAD_PAY");
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        CartItem ci = cartItemWithVariant(u, variantId, 1, 10, true, BigDecimal.valueOf(100), null);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.of(ci.getVariant()));

        var ex = assertThrows(InvalidCheckoutRequestException.class, () -> checkoutService.placeOrder(req));
        assertTrue(ex.getMessage().contains("Invalid payment method"));
    }

    @Test void shouldRejectAddressBelongingToAnotherUser() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setAddressId(99L);
        User u = userWithId(1L);
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findWithAddressesById(1L)).thenReturn(Optional.of(u));
        when(addressRepository.findByIdAndUser_Id(99L, 1L)).thenReturn(Optional.empty());
        var ex = assertThrows(InvalidCheckoutRequestException.class, () -> checkoutService.placeOrder(req));
        assertEquals("Address does not belong to user", ex.getMessage());
    }

    @Test void shouldUsePrimaryAddressWhenAddressIdNotProvided() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setAddressId(null);
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        Address primary = savedAddress(userId);
        primary.setId(55L);
        CartItem ci = cartItemWithVariant(u, variantId, 1, 10, true, BigDecimal.valueOf(100), null);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(primary));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.of(ci.getVariant()));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> { Order o = inv.getArgument(0); o.setId(100L); return o; });

        assertDoesNotThrow(() -> checkoutService.placeOrder(req));
        verify(addressRepository).findByUser_IdAndPrimaryAddressTrue(userId);
    }

    @Test void shouldCreateNewAddressWhenUserHasNoAddress() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setAddressId(null);
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        u.setAddresses(new ArrayList<>());
        CartItem ci = cartItemWithVariant(u, variantId, 1, 10, true, BigDecimal.valueOf(100), null);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.empty());
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.of(ci.getVariant()));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> { Order o = inv.getArgument(0); o.setId(100L); return o; });

        checkoutService.placeOrder(req);

        ArgumentCaptor<Address> cap = ArgumentCaptor.forClass(Address.class);
        verify(addressRepository).save(cap.capture());
        assertEquals(userId, cap.getValue().getUser().getId());
        assertTrue(cap.getValue().isPrimaryAddress());
    }

    @Test void shouldRejectMissingVariant() {
        PlaceCheckoutRequest req = createValidRequest();
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        CartItem ci = cartItemWithVariant(u, variantId, 1, 10, true, BigDecimal.valueOf(100), null);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.empty());

        var ex = assertThrows(InvalidCheckoutRequestException.class, () -> checkoutService.placeOrder(req));
        assertTrue(ex.getMessage().contains("Variant not found"));
    }

    @Test void shouldRejectInactiveVariant() {
        PlaceCheckoutRequest req = createValidRequest();
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        CartItem ci = cartItemWithVariant(u, variantId, 1, 10, false, BigDecimal.valueOf(100), null);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.of(ci.getVariant()));

        assertThrows(InvalidCheckoutRequestException.class, () -> checkoutService.placeOrder(req));
    }

    @Test void shouldRejectInsufficientStock() {
        PlaceCheckoutRequest req = createValidRequest();
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        CartItem ci = cartItemWithVariant(u, variantId, 5, 1, true, BigDecimal.valueOf(100), null);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.of(ci.getVariant()));

        assertThrows(InvalidCheckoutRequestException.class, () -> checkoutService.placeOrder(req));
    }

    @Test void shouldCalculateSubtotalCorrectly() {
        PlaceCheckoutRequest req = createValidRequest();
        long userId = 1L;
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        // two items: 2 * 80 (sale) + 1 * 100 = 260
        CartItem ci1 = cartItemWithVariant(u, 1L, 2, 10, true, BigDecimal.valueOf(100), BigDecimal.valueOf(80));
        CartItem ci2 = cartItemWithVariant(u, 2L, 1, 10, true, BigDecimal.valueOf(100), null);
        ProductVariant v1 = ci1.getVariant(); ProductVariant v2 = ci2.getVariant();
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci1, ci2));
        when(productVariantRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(v1));
        when(productVariantRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(v2));
        when(productVariantRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(v1));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> { Order o = inv.getArgument(0); o.setId(100L); return o; });
        lenient().when(productVariantRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(v1));
        lenient().when(productVariantRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(v2));

        CheckoutInitiationResponse res = checkoutService.placeOrder(req);
        ArgumentCaptor<Order> cap = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(cap.capture());
        // subtotal 260, shipping STANDARD 5000, total 5260
        assertEquals(0, new BigDecimal("260").compareTo(cap.getValue().getSubtotal()));
        assertEquals(0, new BigDecimal("5260").compareTo(cap.getValue().getTotal()));
    }

    @Test void shouldCreateOrderSuccessfully() {
        PlaceCheckoutRequest req = createValidRequest();
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        CartItem ci = cartItemWithVariant(u, variantId, 2, 10, true, BigDecimal.valueOf(100), BigDecimal.valueOf(80));
        ProductVariant v = ci.getVariant();
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.of(v));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> { Order o = inv.getArgument(0); o.setId(100L); return o; });

        CheckoutInitiationResponse res = checkoutService.placeOrder(req);

        assertNotNull(res);
        assertEquals(100L, res.getOrderId());
        assertEquals(1, res.getItems().size());
        assertEquals(PaymentMethod.COD, res.getPaymentMethod());
        assertFalse(res.isPaymentRequired());
        verify(orderRepository).save(any(Order.class));
    }

    @Test void shouldSetProcessingStatusForCOD() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setPaymentMethod("COD");
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        CartItem ci = cartItemWithVariant(u, variantId, 1, 10, true, BigDecimal.valueOf(100), null);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.of(ci.getVariant()));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> { Order o = inv.getArgument(0); o.setId(100L); return o; });

        checkoutService.placeOrder(req);

        ArgumentCaptor<Order> cap = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(cap.capture());
        assertEquals(OrderStatus.PROCESSING, cap.getValue().getOrderStatus());
    }

    @Test void shouldSetPendingPaymentStatusForExternalPayment() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setPaymentMethod("VNPAY");
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        CartItem ci = cartItemWithVariant(u, variantId, 1, 10, true, BigDecimal.valueOf(100), null);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.of(ci.getVariant()));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> { Order o = inv.getArgument(0); o.setId(100L); return o; });

        CheckoutInitiationResponse res = checkoutService.placeOrder(req);

        ArgumentCaptor<Order> cap = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(cap.capture());
        assertEquals(OrderStatus.PENDING_PAYMENT, cap.getValue().getOrderStatus());
        assertTrue(res.isPaymentRequired());
        assertEquals(PaymentMethod.VNPAY, res.getPaymentMethod());
    }

    @Test void shouldDeductStockForCOD() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setPaymentMethod("COD");
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        CartItem ci = cartItemWithVariant(u, variantId, 2, 10, true, BigDecimal.valueOf(100), null);
        ProductVariant v = ci.getVariant();
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.of(v));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> { Order o = inv.getArgument(0); o.setId(100L); return o; });

        checkoutService.placeOrder(req);

        assertEquals(8, v.getStock());
    }

    @Test void shouldClearCartAfterCODOrder() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setPaymentMethod("COD");
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        CartItem ci = cartItemWithVariant(u, variantId, 1, 10, true, BigDecimal.valueOf(100), null);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.of(ci.getVariant()));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> { Order o = inv.getArgument(0); o.setId(100L); return o; });

        checkoutService.placeOrder(req);

        verify(cartItemRepository).deleteAll(anyList());
    }

    @Test void shouldNotDeductStockImmediatelyForExternalPayment() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setPaymentMethod("VNPAY");
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        CartItem ci = cartItemWithVariant(u, variantId, 2, 10, true, BigDecimal.valueOf(100), null);
        ProductVariant v = ci.getVariant();
        int stockBefore = v.getStock();
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.of(v));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> { Order o = inv.getArgument(0); o.setId(100L); return o; });

        checkoutService.placeOrder(req);

        assertEquals(stockBefore, v.getStock());
    }

    @Test void shouldNotClearCartImmediatelyForExternalPayment() {
        PlaceCheckoutRequest req = createValidRequest();
        req.setPaymentMethod("VNPAY");
        long userId = 1L; long variantId = 99L;
        User u = userWithId(userId);
        Address addr = savedAddress(userId);
        CartItem ci = cartItemWithVariant(u, variantId, 1, 10, true, BigDecimal.valueOf(100), null);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findWithAddressesById(userId)).thenReturn(Optional.of(u));
        when(addressRepository.findByUser_IdAndPrimaryAddressTrue(userId)).thenReturn(Optional.of(addr));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.findByUser_Id(userId)).thenReturn(List.of(ci));
        when(productVariantRepository.findByIdForUpdate(variantId)).thenReturn(Optional.of(ci.getVariant()));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> { Order o = inv.getArgument(0); o.setId(100L); return o; });

        checkoutService.placeOrder(req);

        verify(cartItemRepository, never()).deleteAll(anyList());
    }
}
