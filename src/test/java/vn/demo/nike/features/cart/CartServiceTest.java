package vn.demo.nike.features.cart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.demo.nike.features.catalog.cart.dto.request.AddToCartRequest;
import vn.demo.nike.features.catalog.cart.dto.request.UpdateCartItemQuantityRequest;
import vn.demo.nike.features.catalog.cart.dto.response.AddToCartResponse;
import vn.demo.nike.features.catalog.cart.entity.CartItem;
import vn.demo.nike.features.catalog.cart.exception.*;
import vn.demo.nike.features.catalog.cart.repository.CartItemRepository;
import vn.demo.nike.features.catalog.cart.service.CartService;
import vn.demo.nike.features.catalog.product.entity.ProductVariant;
import vn.demo.nike.features.catalog.product.repository.ProductVariantRepository;
import vn.demo.nike.features.user.entity.User;
import vn.demo.nike.features.user.repository.UserRepository;
import vn.demo.nike.features.user.request.CurrentUserProvider;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductVariantRepository productVariantRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void addToCart_whenUserNotLoggedIn_shouldThrowUnauthenticatedUserException() {

        // Arrange
        AddToCartRequest request = new AddToCartRequest();
        request.setVariantId(1L);
        request.setQuantity(2);

        when(currentUserProvider.getCurrentUserId()).thenReturn(null);

        //Act & Assert
        assertThrows(
                UnauthenticatedUserException.class,
                () -> cartService.addToCart(request)
        );

        // Verify
        verifyNoInteractions(cartItemRepository, productVariantRepository, userRepository);
    }

    @Test
    void addToCart_whenVariantDoesNotExist_shouldThrowVariantNotFoundException() {
        // Arrange
        AddToCartRequest request = new AddToCartRequest();
        request.setVariantId(99L);
        request.setQuantity(2);

        Long mockUserId = 1L;
        when(currentUserProvider.getCurrentUserId()).thenReturn(mockUserId);

        User mockUser = new User();
        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));

        when(productVariantRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                VariantNotFoundException.class,
                () -> cartService.addToCart(request)
        );
        verify(productVariantRepository).findById(99L);
        verifyNoInteractions(cartItemRepository);
    }

    @Test
    void addToCart_whenQuantityIsZero_shouldThrowInvalidCartQuantityException() {
        // Arrange
        AddToCartRequest request = new AddToCartRequest();
        request.setVariantId(99L);
        request.setQuantity(0);

        // Act & Assert
        assertThrows(
                InvalidCartQuantityException.class,
                () -> cartService.addToCart(request)
        );

        // Verify
        verifyNoInteractions(cartItemRepository, productVariantRepository, userRepository, currentUserProvider);
    }

    @Test
    void addToCart_whenVariantInactive_shouldThrowInactiveVariantException() {
        // Arrange
        AddToCartRequest request = new AddToCartRequest();
        request.setVariantId(99L);
        request.setQuantity(2);

        User mockUser = new User();
        mockUser.setId(1L);

        ProductVariant mockProductVariant = new ProductVariant();
        mockProductVariant.setId(99L);
        mockProductVariant.setActive(false);

        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(productVariantRepository.findById(99L)).thenReturn(Optional.of(mockProductVariant));

        // Act & Assert
        assertThrows(
                InactiveVariantException.class,
                () -> cartService.addToCart(request)
        );

        // Verify
        verify(productVariantRepository).findById(99L);
        verify(cartItemRepository, never()).findByUser_IdAndVariant_Id(1L, 99L);
        verify(cartItemRepository, never()).save(any());
        }

    @Test
    void addToCart_whenQuantityExceedStock_shouldThrowInsufficientStockException() {
        // Arrange
        AddToCartRequest request = new AddToCartRequest();
        request.setVariantId(99L);
        request.setQuantity(99);

        User mockUser = new User();
        mockUser.setId(1L);

        ProductVariant mockProductVariant = new ProductVariant();
        mockProductVariant.setId(99L);
        mockProductVariant.setActive(true);
        mockProductVariant.setStock(1);

        // Act & Assert
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(productVariantRepository.findById(99L)).thenReturn(Optional.of(mockProductVariant));
        when(cartItemRepository.findByUser_IdAndVariant_Id(1L, 99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                InsufficientStockException.class,
                () -> cartService.addToCart(request)
        );

        // Verify
        verify(cartItemRepository, never()).save(any());
        verify(cartItemRepository, never()).sumQuantityByUser_Id(anyLong());
    }

    @Test
    void addToCart_whenItemAlreadyExists_shouldIncreaseQuantity() {
        // Arrange the request to increase quantity to 3
        AddToCartRequest request = new AddToCartRequest();
        request.setVariantId(99L);
        request.setQuantity(1); // <=

        User mockUser = new User();
        mockUser.setId(1L);

        ProductVariant mockProductVariant = new ProductVariant();
        mockProductVariant.setId(99L);
        mockProductVariant.setActive(true);
        mockProductVariant.setStock(10);

        CartItem mockExistingCartItem = new CartItem();
        mockExistingCartItem.setId(2L);
        mockExistingCartItem.setQuantity(2); // <=
        mockExistingCartItem.setVariant(mockProductVariant);
        mockExistingCartItem.setUser(mockUser);

        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(productVariantRepository.findById(99L)).thenReturn(Optional.of(mockProductVariant));
        when(cartItemRepository.findByUser_IdAndVariant_Id(1L, 99L)).thenReturn(Optional.of(mockExistingCartItem));
        when(cartItemRepository.sumQuantityByUser_Id(1L)).thenReturn(3); // Item count

        // Act & Assert
        AddToCartResponse response = cartService.addToCart(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Added to cart successfully", response.getMessage());
        assertEquals(3, response.getItemCount());

        // Verify
        assertEquals(3, mockExistingCartItem.getQuantity());
        verify(cartItemRepository).save(mockExistingCartItem);
        verify(cartItemRepository).sumQuantityByUser_Id(1L);
    }

    @Test
    void updateCartItemQuantity_whenCartItemDoesNotBelongToUser_shouldThrowCartItemNotFoundException() {
        UpdateCartItemQuantityRequest request = new UpdateCartItemQuantityRequest();
        request.setQuantity(3);

        User mockUser = new User();
        mockUser.setId(1L);

        CartItem mockCartItem = new CartItem();
        mockCartItem.setId(2L);

        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(cartItemRepository.findByIdAndUser_Id(2L, 1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                CartItemNotFoundException.class,
                () -> cartService.updateCartItemQuantity(2L, request)
        );

        // Verify
        verifyNoInteractions(cartItemRepository);
    }
}
