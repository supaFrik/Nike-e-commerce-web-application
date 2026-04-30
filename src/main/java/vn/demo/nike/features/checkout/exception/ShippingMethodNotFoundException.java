package vn.demo.nike.features.checkout.exception;

public class ShippingMethodNotFoundException extends RuntimeException {
    public ShippingMethodNotFoundException(String shippingMethod) {
        super(shippingMethod + " not found");
    }
}
