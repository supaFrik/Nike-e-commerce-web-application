package vn.demo.nike.features.cart.exception;

public class InactiveVariantException extends RuntimeException {
    public InactiveVariantException(Long variantId) {
        super("Variant is inactive: " + variantId);
    }
}
