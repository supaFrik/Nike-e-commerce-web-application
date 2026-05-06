package vn.demo.nike.features.order.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class OrderOverviewView {
    String placedAtLabel;
    String orderStatusLabel;
    String paymentStatusLabel;
    String paymentMethodLabel;
    String paymentSummaryLabel;
    BigDecimal total;
}
