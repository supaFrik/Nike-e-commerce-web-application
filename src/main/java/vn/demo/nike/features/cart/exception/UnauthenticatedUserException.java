package vn.demo.nike.features.cart.exception;

public class UnauthenticatedUserException extends RuntimeException {
    public UnauthenticatedUserException() {
        super("Authentication required");
    }
}
