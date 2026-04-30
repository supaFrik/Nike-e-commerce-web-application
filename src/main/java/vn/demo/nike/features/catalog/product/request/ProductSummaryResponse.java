package vn.demo.nike.features.catalog.product.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductSummaryResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final String categoryName;
    private final String thumbnailUrl;
}



