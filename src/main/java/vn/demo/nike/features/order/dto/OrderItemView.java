package vn.demo.nike.features.order.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class OrderItemView {
    String imageUrl;
    String productName;
    String productUrl;
    String sku;
    String size;
    String color;
    BigDecimal unitPrice;
    Integer quantity;
    BigDecimal lineTotal;
}
