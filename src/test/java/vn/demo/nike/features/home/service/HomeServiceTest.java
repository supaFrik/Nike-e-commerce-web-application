package vn.demo.nike.features.home.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.demo.nike.features.catalog.category.dto.response.RunningSectionView;
import vn.demo.nike.features.catalog.category.service.CategoryService;
import vn.demo.nike.features.catalog.product.dto.request.ProductListItemView;
import vn.demo.nike.features.catalog.product.service.ProductListService;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HomeServiceTest {

    @Test
    void getRunningSection_returnsRunningProductsAndCategoryMetadata() {
        CategoryService categoryService = mock(CategoryService.class);
        ProductListService productListService = mock(ProductListService.class);
        HomeService homeService = new HomeService(categoryService, productListService);

        when(categoryService.getCategoryIdByName("Running")).thenReturn(5L);
        when(productListService.getProductList(5L, "newest")).thenReturn(List.of(
                product(1L, "Nike Vomero 18"),
                product(2L, "Nike Pegasus 41")
        ));

        RunningSectionView runningSection = homeService.getRunningSection();

        assertThat(runningSection.getCategoryId()).isEqualTo(5L);
        assertThat(runningSection.getCategoryName()).isEqualTo("Running");
        assertThat(runningSection.getProducts()).hasSize(2);
        assertThat(runningSection.getProducts().get(0).getName()).isEqualTo("Nike Vomero 18");
    }

    @Test
    void getRunningSection_limitsHomepageProductsToEightItems() {
        CategoryService categoryService = mock(CategoryService.class);
        ProductListService productListService = mock(ProductListService.class);
        HomeService homeService = new HomeService(categoryService, productListService);

        when(categoryService.getCategoryIdByName("Running")).thenReturn(5L);
        when(productListService.getProductList(5L, "newest")).thenReturn(List.of(
                product(1L, "1"),
                product(2L, "2"),
                product(3L, "3"),
                product(4L, "4"),
                product(5L, "5"),
                product(6L, "6"),
                product(7L, "7"),
                product(8L, "8"),
                product(9L, "9")
        ));

        RunningSectionView runningSection = homeService.getRunningSection();

        assertThat(runningSection.getProducts()).hasSize(8);
        assertThat(runningSection.getProducts().get(7).getName()).isEqualTo("8");
    }

    private ProductListItemView product(Long id, String name) {
        return new ProductListItemView(
                id,
                name,
                BigDecimal.valueOf(3500000),
                BigDecimal.ZERO,
                false,
                null,
                "Road Running",
                "Running",
                "/uploads/products/" + name + ".jpg",
                1
        );
    }
}
