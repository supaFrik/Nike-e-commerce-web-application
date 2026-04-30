package vn.demo.nike.features.order.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaymentHistoryView {
    String title;
    String description;
    String timestampLabel;
    boolean success;
}