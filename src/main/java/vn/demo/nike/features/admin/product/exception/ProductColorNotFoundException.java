package vn.demo.nike.features.admin.product.exception;

public class ProductColorNotFoundException extends RuntimeException {
    public ProductColorNotFoundException() {
        super("Product color not found");
    }
}
