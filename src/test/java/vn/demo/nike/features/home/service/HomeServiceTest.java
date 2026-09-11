package vn.demo.nike.features.home.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import vn.demo.nike.features.catalog.category.dto.response.RunningSectionView;
import vn.demo.nike.features.catalog.category.exception.CategoryNotFoundException;
import vn.demo.nike.features.catalog.category.service.CategoryService;
import vn.demo.nike.features.catalog.product.dto.request.ProductListItemView;
import vn.demo.nike.features.catalog.product.service.ProductListService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeServiceTest {

    @Mock private CategoryService categoryService;
    @Mock private ProductListService productListService;
    @InjectMocks private HomeService homeService;

    private ProductListItemView view(long id) {
        return new ProductListItemView(id, "Shoe " + id, BigDecimal.valueOf(100), null, false, null, "Shoes", "Running", "hero.jpg", 2);
    }

    @Test
    void getRunningSection_returnsProductsWhenCategoryExists() {
        when(categoryService.getCategoryIdByName("Running")).thenReturn(7L);
        List<ProductListItemView> ten = IntStream.rangeClosed(1, 10).mapToObj(i -> view(i)).toList();
        when(productListService.getProductList(7L, "newest")).thenReturn(new PageImpl<>(ten));

        RunningSectionView result = homeService.getRunningSection();

        assertEquals(7L, result.getCategoryId());
        assertEquals("Running", result.getCategoryName());
        assertEquals(8, result.getProducts().size());
        assertEquals(1L, result.getProducts().get(0).getId());
    }

    @Test
    void getRunningSection_limitsToEight() {
        when(categoryService.getCategoryIdByName("Running")).thenReturn(3L);
        List<ProductListItemView> twenty = IntStream.rangeClosed(1, 20).mapToObj(i -> view(i)).toList();
        when(productListService.getProductList(3L, "newest")).thenReturn(new PageImpl<>(twenty));

        RunningSectionView result = homeService.getRunningSection();

        assertEquals(8, result.getProducts().size());
    }

    @Test
    void getRunningSection_returnsEmptyWhenCategoryNotFound() {
        when(categoryService.getCategoryIdByName("Running")).thenThrow(new CategoryNotFoundException("Running"));

        RunningSectionView result = homeService.getRunningSection();

        assertNull(result.getCategoryId());
        assertEquals("Running", result.getCategoryName());
        assertTrue(result.getProducts().isEmpty());
        verify(productListService, never()).getProductList(any(), any());
    }

    @Test
    void getRunningSection_returnsEmptyListWhenNoProducts() {
        when(categoryService.getCategoryIdByName("Running")).thenReturn(9L);
        when(productListService.getProductList(9L, "newest")).thenReturn(new PageImpl<>(List.of()));

        RunningSectionView result = homeService.getRunningSection();

        assertEquals(9L, result.getCategoryId());
        assertTrue(result.getProducts().isEmpty());
    }
}
