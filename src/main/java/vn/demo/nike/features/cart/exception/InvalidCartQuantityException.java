package vn.demo.nike.features.cart.exception;

public class InvalidCartQuantityException extends RuntimeException {
    public InvalidCartQuantityException() {
        super("Quantity must be greater than 0");
    }
}
