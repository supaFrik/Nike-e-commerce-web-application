package vn.demo.nike.features.admin.product.exception;

public class InvalidFileMappingException extends RuntimeException {

    public InvalidFileMappingException(String message) {
        super(message);
    }

    public InvalidFileMappingException() {
        super("Invalid file mapping request");
    }
}
