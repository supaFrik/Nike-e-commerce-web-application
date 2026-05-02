package vn.demo.nike.features.catalog.search.dto;

import java.math.BigDecimal;

public interface SearchProductProjection {
    Long getId();
    String getName();
    BigDecimal getPrice();
    BigDecimal getSalePrice();
    String getCategoryName();
    String getImageUrl();
}
