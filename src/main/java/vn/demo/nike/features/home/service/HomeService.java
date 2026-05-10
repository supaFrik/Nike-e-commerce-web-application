package vn.demo.nike.features.home.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.catalog.category.dto.response.RunningSectionView;
import vn.demo.nike.features.catalog.category.service.CategoryService;
import vn.demo.nike.features.catalog.product.dto.request.ProductListItemView;
import vn.demo.nike.features.catalog.product.service.ProductListService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final CategoryService categoryService;
    private final ProductListService productListService;

    public RunningSectionView getRunningSection() {
        Long runningCategoryId = categoryService.getCategoryIdByName("Running");
        List<ProductListItemView> products = productListService.getProductList(runningCategoryId, "newest").stream()
                .limit(8)
                .toList();
        return new RunningSectionView(runningCategoryId, "Running", products);
    }
}
