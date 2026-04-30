package vn.demo.nike.features.catalog.product.exception;

public class InvalidProductStatusException extends RuntimeException {
    public  InvalidProductStatusException() {
        super("Invalid product status");
    }

    public  InvalidProductStatusException(String message) {
        super(message);
    }

    public  InvalidProductStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
