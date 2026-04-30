package vn.demo.nike.features.catalog.product.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductVariantDetailResponse {
    private final Long id;
    private final String sku;
    private final String size;
    private final Integer stock;
    private final Boolean active;
}
