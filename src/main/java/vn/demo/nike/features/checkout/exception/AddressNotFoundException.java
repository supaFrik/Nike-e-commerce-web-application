package vn.demo.nike.features.checkout.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(Long addressId) {
        super(addressId.toString() + " not found");
    }
}
