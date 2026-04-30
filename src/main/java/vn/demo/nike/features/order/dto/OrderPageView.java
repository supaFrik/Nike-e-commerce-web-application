package vn.demo.nike.features.order.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
public class OrderPageView {
    Long orderId;
    String orderCode;
    String successHeadline;
    String successDescription;
    String placedAtLabel;
    String orderStatusLabel;
    String paymentStatusLabel;
    String paymentMethodLabel;
    String paymentSummaryLabel;
    BigDecimal total;

    List<OrderTimelineStepView> timelineSteps;
    List<OrderItemView> items;
    OrderShippingView shipping;
    OrderPaymentView payment;
    OrderPricingView pricing;
    List<OrderActionView> actions;
    List<PaymentHistoryView> paymentHistory;
}
