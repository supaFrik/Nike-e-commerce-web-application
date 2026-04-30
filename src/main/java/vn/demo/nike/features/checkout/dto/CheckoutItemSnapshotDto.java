package vn.demo.nike.features.checkout.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutItemSnapshotDto {
    private Long productId;
    private Long variantId;
    private String sku;
    private String productName;
    private String size;
    private String color;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal lineTotal;
}
