package vn.demo.nike.infras.payment.vnpay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.demo.nike.infras.payment.vnpay.enums.PaymentStatus;

@Getter
@AllArgsConstructor
public class VNPayReturnResponse {
    private final Long orderId;
    private final String txnRef;
    private final boolean signatureValid;
    private final String responseCode;
    private final String transactionStatus;
    private final PaymentStatus suggestedPaymentStatus;
}
