package vn.demo.nike.features.catalog.cart.exception;

public class UnauthenticatedUserException extends RuntimeException {
    public UnauthenticatedUserException() {
        super("Authentication required");
    }
}
