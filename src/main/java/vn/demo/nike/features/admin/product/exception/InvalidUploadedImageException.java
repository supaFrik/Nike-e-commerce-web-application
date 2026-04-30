package vn.demo.nike.features.admin.product.exception;

public class InvalidUploadedImageException extends RuntimeException {

    public InvalidUploadedImageException(String message) {
        super(message);
    }

    public InvalidUploadedImageException() {
        super("Invalid uploaded image");
    }
}
