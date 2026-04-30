package vn.demo.nike.features.cart.exception;

public class VariantNotFoundException extends RuntimeException {
    public VariantNotFoundException(Long variantId) {
        super("Variant not found: " + variantId);
    }
}
