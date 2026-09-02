package vn.demo.nike.features.checkout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import vn.demo.nike.features.checkout.exception.InvalidCheckoutRequestException;
import vn.demo.nike.features.checkout.exception.UnauthenticatedCheckoutException;
import vn.demo.nike.features.checkout.service.CheckoutService;
import vn.demo.nike.features.order.repository.OrderRepository;
import vn.demo.nike.features.user.entity.Address;
import vn.demo.nike.features.user.entity.User;
import vn.demo.nike.features.user.repository.AddressRepository;
import vn.demo.nike.features.user.repository.UserRepository;
import vn.demo.nike.features.user.request.CurrentUserProvider;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceTest {
    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductVariantRepository productVariantRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private CheckoutService checkoutService;

    // TODO: Implement unit tests for CheckoutService
    // 1. shouldRejectNullRequest
    // 2. shouldRejectUnauthenticatedUser
    //3. shouldRejectEmptyCart
    //4. shouldRejectMissingRecipientName
    //5. shouldRejectMissingPhone
    //6. shouldRejectMissingAddress
    //7. shouldRejectMissingCity
    //8. shouldRejectMissingCountry
    //9. shouldUseStandardShippingWhenNotProvided
    //10. shouldRejectInvalidShippingMethod
    //11. shouldRejectMissingPaymentMethod
    //12. shouldRejectInvalidPaymentMethod
    //13. shouldRejectAddressBelongingToAnotherUser
    //14. shouldUsePrimaryAddressWhenAddressIdNotProvided
    //15. shouldCreateNewAddressWhenUserHasNoAddress
    //16. shouldRejectInactiveVariant
    //17. shouldRejectInsufficientStock
    //18. shouldRejectMissingVariant
    //19. shouldCalculateSubtotalCorrectly
    //20. shouldCreateOrderSuccessfully
    //21. shouldSetProcessingStatusForCOD
    //22. shouldSetPendingPaymentStatusForExternalPayment
    //23. shouldDeductStockForCOD
    //24. shouldClearCartAfterCODOrder
    //25. shouldNotDeductStockImmediatelyForExternalPayment
    //26. shouldNotClearCartImmediatelyForExternalPayment
    @Test
    void shouldRejectNullRequest() {
        PlaceCheckoutRequest request = null;
        assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(request)
        );
    }

    @Test
    void shouldRejectUnauthenticatedUser() {
        User mockUser = new User();
        when(currentUserProvider.getCurrentUserId()).thenReturn(null);
        assertThrows(
                UnauthenticatedCheckoutException.class,
                () -> checkoutService.placeOrder(new PlaceCheckoutRequest())
        );
    }

    @Test
    void shouldRejectEmptyCart() {
        User mockUser = new User();
        mockUser.setId(1L);

        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(cartItemRepository.findByUser_Id(1L)).thenReturn(List.of());

        assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(new PlaceCheckoutRequest())
        );
    }


    @Test
    void shouldRejectMissingRecipientName() {
        PlaceCheckoutRequest request = createValidRequest();
        request.setRecipientName(null);

        InvalidCheckoutRequestException exception = assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(request)
        );

        assertEquals("Recipient name is required", exception.getMessage());
    }

    @Test
    void shouldRejectMissingPhone() {
        PlaceCheckoutRequest request = createValidRequest();
        request.setPhone(null);

        InvalidCheckoutRequestException exception = assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(request)
        );

        assertEquals("Phone is required", exception.getMessage());
    }

    @Test
    void shouldRejectMissingAddress() {
        PlaceCheckoutRequest request = createValidRequest();
        request.setLine1(null);

        InvalidCheckoutRequestException exception = assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(request)
        );

        assertEquals("Address is required", exception.getMessage());
    }

    @Test
    void shouldRejectMissingCity() {
        PlaceCheckoutRequest request = createValidRequest();
        request.setCity(null);

        InvalidCheckoutRequestException exception = assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(request)
        );

        assertEquals("City is required", exception.getMessage());
    }

    @Test
    void shouldRejectMissingCountry() {
        PlaceCheckoutRequest request = createValidRequest();
        request.setCountry(null);

        InvalidCheckoutRequestException exception = assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(request)
        );

        assertEquals("Country is required", exception.getMessage());
    }

    @Test
    void shouldRejectUnauthenticatedUserWhenCurrentUserIdIsNull() {
        PlaceCheckoutRequest request = createValidRequest();

        when(currentUserProvider.getCurrentUserId()).thenReturn(null);

        assertThrows(
                UnauthenticatedCheckoutException.class,
                () -> checkoutService.placeOrder(request)
        );

        verify(userRepository, never()).findWithAddressesById(anyLong());
    }

    @Test
    void shouldRejectUnauthenticatedUserWhenCurrentUserDoesNotExist() {
        PlaceCheckoutRequest request = createValidRequest();

        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findWithAddressesById(1L)).thenReturn(Optional.empty());

        assertThrows(
                UnauthenticatedCheckoutException.class,
                () -> checkoutService.placeOrder(request)
        );

    }

    @Test
    void shouldUseStandardShippingWhenShippingMethodIsMissing() {
        PlaceCheckoutRequest request = createValidRequest();
        request.setShippingMethod(null);

        User user = new User();
        user.setId(1L);

        when(userRepository.findWithAddressesById(1L)).thenReturn(Optional.of(user));
        when(request.getShippingMethod()).thenReturn(null);

        assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(request)
        );

        verify(userRepository, never()).findWithAddressesById(anyLong());
        verify(request).getShippingMethod();
    }


    @Test
    void shouldRejectInvalidShippingMethod() {
        PlaceCheckoutRequest request = createValidRequest();
        request.setShippingMethod("INVALID_METHOD");

        assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(request)
        );

        verify(request).getShippingMethod();
    }

    @Test
    void shouldRejectAddressBelongingToAnotherUser() {

    }

    @Test
    void shouldUsePrimaryAddressWhenAddressIdNotProvided() {
        User user = new User();
        user.setId(1L);

        PlaceCheckoutRequest request = createValidRequest();
        request.setAddressId(null);

        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findWithAddressesById(1L)).thenReturn(Optional.of(user));
        when(request.getAddressId()).thenReturn(1L);

        assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(request)
        );

        verify(userRepository, never()).findWithAddressesById(anyLong());
    }

//    @Test
//    void shouldCreateNewAddressWhenUserHasNoAddress() {
//
//    }


    @Test
    void shouldRejectVariantNotFound() {
        ProductVariant variant = new ProductVariant();
        variant.setId(99L);

        when(productVariantRepository.findByIdForUpdate(99L)).thenReturn(Optional.empty());

        assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(createValidRequest())
        );
    }

    @Test
    void shouldRejectInactiveVariant() {
        ProductVariant variant = new ProductVariant();
        variant.setId(99L);
        variant.setActive(false);

        when(productVariantRepository.findByIdForUpdate(99L)).thenReturn(Optional.of(variant));

        assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(createValidRequest())
        );
    }

    @Test
    void shouldRejectVariantWithNullStock() {

        ProductVariant variant = new ProductVariant();
        variant.setId(99L);
        variant.setActive(true);
        variant.setStock(null);

        when(productVariantRepository.findByIdForUpdate(99L)).thenReturn(Optional.of(variant));

        assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(createValidRequest())
        );
    }

    @Test
    void shouldRejectInsufficientStock() {
        ProductVariant variant = new ProductVariant();
        variant.setId(99L);
        variant.setActive(true);
        variant.setStock(1);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(2);

        when(productVariantRepository.findByIdForUpdate(99L)).thenReturn(Optional.of(variant));
        when(variant.getStock() < cartItem.getQuantity()).thenReturn(true);

        assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(createValidRequest())
        );
        verify(productVariantRepository, never()).findByIdForUpdate(anyLong());
    }

    @Test
    void shouldUseSalePriceWhenAvailable() {
        ProductVariant variant = new ProductVariant();
        ProductColor color = variant.getColor();
        Product product = color.getProduct();
        product.setSalePrice(BigDecimal.valueOf(50.0));

        when(product.getSalePrice()).thenReturn(BigDecimal.valueOf(50.0));

        assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(createValidRequest())
        );
    }

    @Test
    void shouldUseRegularPriceWhenSalePriceIsNull() {
        ProductVariant variant = new ProductVariant();
        ProductColor color = variant.getColor();
        Product product = color.getProduct();
        product.setSalePrice(null);
        product.setPrice(BigDecimal.valueOf(100.0));

        when(product.getSalePrice()).thenReturn(null);
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(100.0));

        assertThrows(
                InvalidCheckoutRequestException.class,
                () -> checkoutService.placeOrder(createValidRequest())
        );
    }

    @Test
    void shouldCalculateSubtotalCorrectly() {

    }

    @Test
    void shouldRejectMissingPaymentMethod() {

    }

    @Test
    void shouldRejectInvalidPaymentMethod() {

    }

    @Test
    void shouldSetCODOrderStatusToProcessing() {

    }

    @Test
    void shouldCreateOrderSuccessfully() {

    }

    @Test
    void shouldDeductStockForCODOrder() {

    }

    @Test
    void shouldClearCartAfterCODOrder() {

    }

    @Test
    void shouldNotDeductStockImmediatelyForExternalPayment() {

    }

    private PlaceCheckoutRequest createValidRequest() {
    PlaceCheckoutRequest request = new PlaceCheckoutRequest();

    request.setRecipientName("John Doe");
    request.setPhone("0123456789");
    request.setLine1("123 Nguyen Trai");
    request.setCity("Hanoi");
    request.setCountry("Vietnam");
    request.setPaymentMethod("COD");

    return request;
}
}

