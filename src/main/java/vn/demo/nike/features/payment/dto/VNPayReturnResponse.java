package vn.demo.nike.features.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.demo.nike.features.payment.domain.enums.PaymentStatus;

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
