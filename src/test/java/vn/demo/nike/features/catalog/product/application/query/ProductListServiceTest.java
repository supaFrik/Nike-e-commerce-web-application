package vn.demo.nike.features.catalog.product.application.query;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import vn.demo.nike.features.catalog.product.dto.request.ProductListItemView;
import vn.demo.nike.features.catalog.product.dto.response.ProductQueryResponseMapper;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;
import vn.demo.nike.features.catalog.product.service.ProductListService;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductListServiceTest {

    @Test
    void getProductList_returnsMappedItems() {
        ProductRepository productRepository = mock(ProductRepository.class);
        ProductQueryResponseMapper productQueryResponseMapper = new ProductQueryResponseMapper();
        ProductListService productListService =
                new ProductListService(productRepository, productQueryResponseMapper);

        when(productRepository.findProductList(eq(5L), any(Pageable.class))).thenReturn(List.of(
                new ProductListItemView(
                        11L,
                        "Nike Vomero 18",
                        BigDecimal.valueOf(3500000),
                        BigDecimal.ZERO,
                        false,
                        null,
                        "Road Running",
                        "Running",
                        "products/vomero/hero.jpg",
                        3
                )
        ));

        List<ProductListItemView> result = productListService.getProductList(5L, "newest");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Nike Vomero 18");
        assertThat(result.get(0).getHeroImg()).isEqualTo("/uploads/products/products/vomero/hero.jpg");
    }
}
