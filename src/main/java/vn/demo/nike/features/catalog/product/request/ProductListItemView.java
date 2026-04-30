package vn.demo.nike.features.catalog.product.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.demo.nike.features.catalog.product.domain.enums.ProductStatus;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductListItemView {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final BigDecimal salePrice;
    private final boolean hasSale;
    private final ProductStatus status;
    private final String type;
    private final String categoryName;
    private final String heroImg;
    private final int colorCount;
}