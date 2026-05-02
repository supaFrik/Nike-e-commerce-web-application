package vn.demo.nike.features.catalog.cart.exception;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(Long cartItemId) {
        super("Cart item not found: " + cartItemId);
    }
}
