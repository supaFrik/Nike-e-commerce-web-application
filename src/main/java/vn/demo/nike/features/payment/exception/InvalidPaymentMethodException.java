package vn.demo.nike.features.payment.exception;

import vn.demo.nike.features.payment.enums.PaymentMethod;

public class InvalidPaymentMethodException extends RuntimeException {
    public InvalidPaymentMethodException(PaymentMethod paymentMethod) {
        super(String.format("Invalid PaymentMethod: %s", paymentMethod));
    }
}
