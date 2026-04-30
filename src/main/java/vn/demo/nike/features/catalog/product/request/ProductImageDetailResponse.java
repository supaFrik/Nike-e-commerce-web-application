package vn.demo.nike.features.catalog.product.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductImageDetailResponse {
    private final Long id;
    private final String path;
    private final String title;
    private final String altText;
    private final Boolean isMainForColor;
    private final Integer orderIndex;
}
