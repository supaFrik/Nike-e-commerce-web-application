package vn.demo.nike.features.checkout.exception;

public class InvalidCheckoutRequestException extends RuntimeException {
    public InvalidCheckoutRequestException(String message)  {
        super(message);
    }
}
