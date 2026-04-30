package vn.demo.nike.features.order.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderPaymentView {
    boolean online;
    String providerLabel;
    String transactionNo;
    String paymentTimeLabel;
    String txnRef;
    String description;
}
