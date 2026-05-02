package vn.demo.nike.features.catalog.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductSearchItemResponse {
    Long id;
    String name;
    BigDecimal price;
    BigDecimal salePrice;
    boolean hasSale;
    String categoryName;
    String imageUrl;
}
