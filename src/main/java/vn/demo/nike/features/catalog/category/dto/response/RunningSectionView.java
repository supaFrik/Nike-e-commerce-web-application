package vn.demo.nike.features.catalog.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.demo.nike.features.catalog.product.dto.request.ProductListItemView;

import java.util.List;

@Getter
@AllArgsConstructor
public class RunningSectionView {
    private final Long categoryId;
    private final String categoryName;
    private final List<ProductListItemView> products;
}
