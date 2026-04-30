package vn.demo.nike.features.catalog.product.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProductColorDetailResponse {
    private final Long id;
    private final String colorName;
    private final String hexCode;
    private final Integer displayOrder;
    private final List<ProductImageDetailResponse> images;
    private final List<ProductVariantDetailResponse> variants;
}
