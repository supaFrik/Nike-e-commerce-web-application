package vn.demo.nike.features.order.dto;

import lombok.Builder;
import lombok.Value;
import vn.demo.nike.features.order.domain.enums.OrderStatus;
import vn.demo.nike.features.payment.domain.enums.PaymentMethod;
import vn.demo.nike.features.payment.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
