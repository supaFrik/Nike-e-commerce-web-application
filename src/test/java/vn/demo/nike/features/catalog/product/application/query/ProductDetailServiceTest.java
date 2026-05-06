package vn.demo.nike.features.catalog.product.application.query;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.demo.nike.features.catalog.category.entity.Category;
import vn.demo.nike.features.catalog.product.dto.response.ProductDetailResponse;
import vn.demo.nike.features.catalog.product.dto.response.ProductQueryResponseMapper;
import vn.demo.nike.features.catalog.product.entity.Product;
import vn.demo.nike.features.catalog.product.entity.ProductColor;
import vn.demo.nike.features.catalog.product.entity.ProductImage;
import vn.demo.nike.features.catalog.product.entity.ProductVariant;
import vn.demo.nike.features.catalog.product.enums.ProductStatus;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;
import vn.demo.nike.features.catalog.product.service.ProductDetailService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductDetailServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductDetailService productDetailService;

    private final ProductQueryResponseMapper productQueryResponseMapper = new ProductQueryResponseMapper();

    @Test
    void getProductDetail_returnsMappedResponse() {
        Product product = new Product();
        product.setId(10L);
        product.setName("Nike Pegasus 41");
        product.setPrice(BigDecimal.valueOf(3200000));
        product.setSalePrice(BigDecimal.valueOf(2900000));
        product.setDescription("Daily trainer");
        product.setProductStatus(ProductStatus.ACTIVE);

        Category category = new Category();
        category.setName("Running");
        product.setCategory(category);

        ProductImage image = new ProductImage();
        image.setId(100L);
        image.setPath("products/pegasus/main.jpg");
        image.setIsMainForColor(true);
        image.setOrderIndex(1);

        ProductVariant variant = new ProductVariant();
        variant.setId(200L);
        variant.setSku("PEG41-BLK-42");
        variant.setSize("42");
        variant.setStock(5);
        variant.setActive(true);

        ProductColor color = new ProductColor();
        color.setId(300L);
        color.setColorName("Black");
        color.setHexCode("#000000");
        color.setDisplayOrder(1);
        color.setImages(List.of(image));
        color.setVariants(List.of(variant));
        product.setColors(List.of(color));

        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        ProductDetailResponse response = new ProductDetailService(productRepository, productQueryResponseMapper)
                .getProductDetail(10L);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getCategoryName()).isEqualTo("Running");
        assertThat(response.getColors()).hasSize(1);
        assertThat(response.getColors().get(0).getImages().get(0).getPath())
                .isEqualTo("/uploads/products/products/pegasus/main.jpg");
    }
}
