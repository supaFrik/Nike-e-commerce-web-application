package vn.demo.nike.features.admin.product.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class InvalidProductColorException extends RuntimeException {
    public InvalidProductColorException(String message) {
        super("Invalid product color: " + message);
    }
}
