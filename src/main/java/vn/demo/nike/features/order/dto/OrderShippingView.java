package vn.demo.nike.features.order.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderShippingView {
    String recipientName;
    String phone;
    String fullAddress;
    String shippingMethodLabel;
}
