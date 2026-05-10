package vn.demo.nike.features.catalog.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductCartResponse {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private BigDecimal salePrice;
    private String url;
}
