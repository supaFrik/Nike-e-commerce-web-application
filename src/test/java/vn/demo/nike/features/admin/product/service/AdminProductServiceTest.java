package vn.demo.nike.features.admin.product.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import vn.demo.nike.features.admin.product.dto.AdminProductColorRequest;
import vn.demo.nike.features.admin.product.dto.AdminProductCreateRequest;
import vn.demo.nike.features.admin.product.dto.AdminProductFormResponse;
import vn.demo.nike.features.admin.product.dto.AdminProductImageRequest;
import vn.demo.nike.features.admin.product.dto.AdminProductVariantRequest;
import vn.demo.nike.features.catalog.category.domain.Category;
import vn.demo.nike.features.catalog.category.repository.CategoryRepository;
import vn.demo.nike.features.catalog.product.domain.Product;
import vn.demo.nike.features.catalog.product.domain.ProductColor;
import vn.demo.nike.features.catalog.product.domain.ProductImage;
import vn.demo.nike.features.catalog.product.domain.ProductVariant;
import vn.demo.nike.features.catalog.product.domain.enums.ProductStatus;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductImageStorageService productImageStorageService;

    @InjectMocks
    private AdminProductService adminProductService;

    @Test
    void getProductForm_returnsRealVariantInventoryFromBackend() {
        Product product = sampleProduct();
        when(productRepository.findDetailById(7L)).thenReturn(Optional.of(product));

        AdminProductFormResponse response = adminProductService.getProductForm(7L);

        assertThat(response.productId()).isEqualTo(7L);
        assertThat(response.colors()).hasSize(1);
        assertThat(response.colors().get(0).variants()).extracting(AdminProductFormResponse.AdminVariantFormItem::stock)
                .containsExactly(11, 4);
        assertThat(response.colors().get(0).images()).extracting(AdminProductFormResponse.AdminImageFormItem::existingImageId)
                .containsExactly(31L, 32L);
    }

    @Test
    void updateProduct_replacesColorBlockAndDeletesRemovedPhysicalImages() {
        Product product = sampleProduct();
        Category category = Category.builder().name("Running").build();
        category.setId(1L);

        when(productRepository.findDetailById(7L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(productImageStorageService).delete("air-max/black/secondary.webp");

        AdminProductCreateRequest request = buildUpdateRequest();

        adminProductService.updateProduct(7L, request, List.of(), List.of());

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        Product saved = productCaptor.getValue();

        assertThat(saved.getColors()).hasSize(1);
        ProductColor savedColor = saved.getColors().get(0);
        assertThat(savedColor.getVariants()).extracting(ProductVariant::getStock).containsExactly(7, 3);
        assertThat(savedColor.getImages()).hasSize(1);
        assertThat(savedColor.getImages().get(0).getPath()).isEqualTo("air-max/black/main.webp");
        verify(productImageStorageService).delete("air-max/black/secondary.webp");
    }

    @Test
    void deleteProduct_removesAggregateAndDeletesAllPhysicalImages() {
        Product product = sampleProduct();
        when(productRepository.findDetailById(7L)).thenReturn(Optional.of(product));

        adminProductService.deleteProduct(7L);

        verify(productRepository).delete(product);
        verify(productImageStorageService).delete("air-max/black/main.webp");
        verify(productImageStorageService).delete("air-max/black/secondary.webp");
    }

    private AdminProductCreateRequest buildUpdateRequest() {
        AdminProductVariantRequest variant41 = new AdminProductVariantRequest();
        ReflectionTestUtils.setField(variant41, "sku", "DN8-BLACK-41");
        ReflectionTestUtils.setField(variant41, "size", "41");
        ReflectionTestUtils.setField(variant41, "stock", 7);
        ReflectionTestUtils.setField(variant41, "active", true);

        AdminProductVariantRequest variant42 = new AdminProductVariantRequest();
        ReflectionTestUtils.setField(variant42, "sku", "DN8-BLACK-42");
        ReflectionTestUtils.setField(variant42, "size", "42");
        ReflectionTestUtils.setField(variant42, "stock", 3);
        ReflectionTestUtils.setField(variant42, "active", true);

        AdminProductImageRequest image = new AdminProductImageRequest();
        ReflectionTestUtils.setField(image, "existingImageId", 31L);
        ReflectionTestUtils.setField(image, "orderIndex", 0);
        ReflectionTestUtils.setField(image, "title", "Main");
        ReflectionTestUtils.setField(image, "altText", "Main image");
        ReflectionTestUtils.setField(image, "isMainForColor", true);

        AdminProductColorRequest color = new AdminProductColorRequest();
        ReflectionTestUtils.setField(color, "colorName", "Black");
        ReflectionTestUtils.setField(color, "hexCode", "#111111");
        ReflectionTestUtils.setField(color, "displayOrder", 0);
        ReflectionTestUtils.setField(color, "images", List.of(image));
        ReflectionTestUtils.setField(color, "variants", List.of(variant41, variant42));

        AdminProductCreateRequest request = new AdminProductCreateRequest();
        ReflectionTestUtils.setField(request, "name", "Air Max Dn8");
        ReflectionTestUtils.setField(request, "description", "Updated description");
        ReflectionTestUtils.setField(request, "type", "MEN");
        ReflectionTestUtils.setField(request, "categoryId", 1L);
        ReflectionTestUtils.setField(request, "productStatus", ProductStatus.ACTIVE);
        ReflectionTestUtils.setField(request, "price", BigDecimal.valueOf(190));
        ReflectionTestUtils.setField(request, "salePrice", BigDecimal.valueOf(169));
        ReflectionTestUtils.setField(request, "colors", List.of(color));
        return request;
    }

    private Product sampleProduct() {
        Product product = new Product();
        product.setId(7L);
        product.setName("Air Max Dn8");
        product.setDescription("Original description");
        product.setType("MEN");
        product.setPrice(BigDecimal.valueOf(190));
        product.setSalePrice(BigDecimal.valueOf(169));
        product.setProductStatus(ProductStatus.ACTIVE);

        Category category = Category.builder().name("Running").build();
        category.setId(1L);
        product.setCategory(category);

        ProductColor color = new ProductColor();
        color.setId(21L);
        color.setColorName("Black");
        color.setHexCode("#111111");
        color.setDisplayOrder(0);
        color.setProduct(product);

        ProductImage mainImage = new ProductImage();
        mainImage.setId(31L);
        mainImage.setPath("air-max/black/main.webp");
        mainImage.setTitle("Main");
        mainImage.setAltText("Main image");
        mainImage.setOrderIndex(0);
        mainImage.setIsMainForColor(true);
        mainImage.setColor(color);

        ProductImage secondaryImage = new ProductImage();
        secondaryImage.setId(32L);
        secondaryImage.setPath("air-max/black/secondary.webp");
        secondaryImage.setTitle("Side");
        secondaryImage.setAltText("Side image");
        secondaryImage.setOrderIndex(1);
        secondaryImage.setIsMainForColor(false);
        secondaryImage.setColor(color);

        ProductVariant variant41 = new ProductVariant();
        variant41.setId(41L);
        variant41.setSku("DN8-BLACK-41");
        variant41.setSize("41");
        variant41.setStock(11);
        variant41.setActive(true);
        variant41.setColor(color);

        ProductVariant variant42 = new ProductVariant();
        variant42.setId(42L);
        variant42.setSku("DN8-BLACK-42");
        variant42.setSize("42");
        variant42.setStock(4);
        variant42.setActive(true);
        variant42.setColor(color);

        color.setImages(new ArrayList<>(List.of(mainImage, secondaryImage)));
        color.setVariants(new ArrayList<>(List.of(variant41, variant42)));
        product.setColors(new ArrayList<>(List.of(color)));
        return product;
    }
}
