package vn.demo.nike.features.catalog.product.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProductDetailResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final BigDecimal salePrice;
    private final String description;
    private final String categoryName;
    private final String productStatus;
    private final List<ProductColorDetailResponse> colors;
}
