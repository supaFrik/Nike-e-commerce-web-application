package vn.demo.nike.features.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VNPayCreatePaymentResponse {
    private final Long orderId;
    private final String txnRef;
    private final String paymentUrl;
}
