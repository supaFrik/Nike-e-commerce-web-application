package vn.demo.nike.features.admin.product.exception;

public class InvalidSalePriceException extends RuntimeException {
    public InvalidSalePriceException() {
        super("Invalid sale price");
    }
}
