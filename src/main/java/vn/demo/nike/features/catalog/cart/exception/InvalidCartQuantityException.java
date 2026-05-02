package vn.demo.nike.features.catalog.cart.exception;

public class InvalidCartQuantityException extends RuntimeException {
    public InvalidCartQuantityException() {
        super("Quantity must be greater than 0");
    }
}
