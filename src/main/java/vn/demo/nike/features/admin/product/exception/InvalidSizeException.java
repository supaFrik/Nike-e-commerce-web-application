package vn.demo.nike.features.admin.product.exception;

public class InvalidSizeException extends RuntimeException {
    public InvalidSizeException(String message) {
        super("Invalid product size: " + message);
    }
}
