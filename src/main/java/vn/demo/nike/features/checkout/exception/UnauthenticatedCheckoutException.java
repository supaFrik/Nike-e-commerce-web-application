package vn.demo.nike.features.checkout.exception;

public class UnauthenticatedCheckoutException extends RuntimeException {
    public UnauthenticatedCheckoutException() {
        super("Authentication required for checkout");
    }
}
