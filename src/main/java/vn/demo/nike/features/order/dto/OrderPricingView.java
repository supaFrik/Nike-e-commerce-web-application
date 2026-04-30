package vn.demo.nike.features.order.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class OrderPricingView {
    BigDecimal subtotal;
    BigDecimal shippingFee;
    BigDecimal discount;
    BigDecimal total;
}