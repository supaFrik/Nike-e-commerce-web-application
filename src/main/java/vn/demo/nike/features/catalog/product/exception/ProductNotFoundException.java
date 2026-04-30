package vn.demo.nike.features.catalog.product.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product with ID: " + id + " is not existed !");
    }
}
