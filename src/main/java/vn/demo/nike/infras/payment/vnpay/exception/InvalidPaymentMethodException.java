package vn.demo.nike.infras.payment.vnpay.exception;

import vn.demo.nike.infras.payment.vnpay.enums.PaymentMethod;

public class InvalidPaymentMethodException extends RuntimeException {
    public InvalidPaymentMethodException(PaymentMethod paymentMethod) {
        super(String.format("Invalid PaymentMethod: %s", paymentMethod));
    }
}
