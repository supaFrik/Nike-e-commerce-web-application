package vn.demo.nike.features.catalog.product.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.demo.nike.features.catalog.product.dto.response.ProductDetailResponse;
import vn.demo.nike.features.catalog.product.dto.response.ProductQueryResponseMapper;
import vn.demo.nike.features.catalog.product.entity.Product;
import vn.demo.nike.features.catalog.product.entity.ProductColor;
import vn.demo.nike.features.catalog.product.exception.ProductNotFoundException;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDetailServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private ProductQueryResponseMapper mapper;
    @InjectMocks private ProductDetailService service;

    @Test
    void getProductDetail_throwsWhenNotFound() {
        when(productRepository.findDetailById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.getProductDetail(99L));
    }

    @Test
    void getProductDetail_returnsMappedResponseWhenFound() {
        Product product = mock(Product.class);
        ProductColor color = mock(ProductColor.class);
        when(color.getImages()).thenReturn(List.of());
        when(color.getVariants()).thenReturn(List.of());
        when(product.getColors()).thenReturn(List.of(color));
        when(productRepository.findDetailById(1L)).thenReturn(Optional.of(product));

        ProductDetailResponse expected = mock(ProductDetailResponse.class);
        when(mapper.toProductDetailResponse(product)).thenReturn(expected);

        ProductDetailResponse result = service.getProductDetail(1L);

        assertSame(expected, result);
        verify(productRepository).findDetailById(1L);
        verify(mapper).toProductDetailResponse(product);
    }

    @Test
    void getProductDetail_initializesLazyCollections() {
        Product product = mock(Product.class);
        ProductColor color = mock(ProductColor.class);
        when(color.getImages()).thenReturn(List.of(mock(vn.demo.nike.features.catalog.product.entity.ProductImage.class)));
        when(color.getVariants()).thenReturn(List.of(mock(vn.demo.nike.features.catalog.product.entity.ProductVariant.class)));
        when(product.getColors()).thenReturn(List.of(color));
        when(productRepository.findDetailById(1L)).thenReturn(Optional.of(product));
        when(mapper.toProductDetailResponse(product)).thenReturn(mock(ProductDetailResponse.class));

        service.getProductDetail(1L);

        verify(color).getImages();
        verify(color).getVariants();
    }
}
