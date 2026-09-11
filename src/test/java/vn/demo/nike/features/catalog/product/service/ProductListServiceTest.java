package vn.demo.nike.features.catalog.product.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.demo.nike.features.catalog.product.dto.request.ProductListItemView;
import vn.demo.nike.features.catalog.product.dto.response.ProductQueryResponseMapper;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductListServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private ProductQueryResponseMapper mapper;
    @InjectMocks private ProductListService service;

    private ProductListItemView view(long id) {
        return new ProductListItemView(id, "Air Max", BigDecimal.valueOf(200), null, false, null, "Shoes", "Running", "hero.jpg", 3);
    }

    @Test
    void getProductList_clampsNegativePageToZero() {
        Page<ProductListItemView> page = new PageImpl<>(List.of(view(1)));
        when(productRepository.findProductList(any(), any(Pageable.class))).thenReturn(page);
        when(mapper.toProductListItemView(any())).thenAnswer(i -> i.getArgument(0));

        Page<ProductListItemView> result = service.getProductList(5L, "newest", -3);

        assertEquals(1, result.getTotalElements());
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findProductList(eq(5L), captor.capture());
        assertEquals(0, captor.getValue().getPageNumber());
        assertEquals(20, captor.getValue().getPageSize());
    }

    @Test
    void getProductList_resolvesPriceAscSort() {
        Page<ProductListItemView> page = new PageImpl<>(List.of(view(1)));
        when(productRepository.findProductList(any(), any(Pageable.class))).thenReturn(page);
        when(mapper.toProductListItemView(any())).thenAnswer(i -> i.getArgument(0));

        service.getProductList(1L, "price_asc", 0);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findProductList(eq(1L), captor.capture());
        assertTrue(captor.getValue().getSort().getOrderFor("price").isAscending());
    }

    @Test
    void getProductList_resolvesPriceDescSort() {
        Page<ProductListItemView> page = new PageImpl<>(List.of(view(1)));
        when(productRepository.findProductList(any(), any(Pageable.class))).thenReturn(page);
        when(mapper.toProductListItemView(any())).thenAnswer(i -> i.getArgument(0));

        service.getProductList(1L, "price_desc", 0);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findProductList(eq(1L), captor.capture());
        assertTrue(captor.getValue().getSort().getOrderFor("price").isDescending());
    }

    @Test
    void getProductList_resolvesNewestSort() {
        Page<ProductListItemView> page = new PageImpl<>(List.of(view(1)));
        when(productRepository.findProductList(any(), any(Pageable.class))).thenReturn(page);
        when(mapper.toProductListItemView(any())).thenAnswer(i -> i.getArgument(0));

        service.getProductList(1L, "newest", 0);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findProductList(eq(1L), captor.capture());
        assertEquals("createDate", captor.getValue().getSort().getOrderFor("createDate").getProperty());
        assertTrue(captor.getValue().getSort().getOrderFor("createDate").isDescending());
    }

    @Test
    void getProductList_defaultSortIsNewest() {
        Page<ProductListItemView> page = new PageImpl<>(List.of(view(1)));
        when(productRepository.findProductList(any(), any(Pageable.class))).thenReturn(page);
        when(mapper.toProductListItemView(any())).thenAnswer(i -> i.getArgument(0));

        service.getProductList(1L, "unknown", 0);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findProductList(eq(1L), captor.capture());
        assertEquals("createDate", captor.getValue().getSort().getOrderFor("createDate").getProperty());
    }

    @Test
    void getProductList_twoArgOverloadDefaultsToPageZero() {
        Page<ProductListItemView> page = new PageImpl<>(List.of(view(1)));
        when(productRepository.findProductList(any(), any(Pageable.class))).thenReturn(page);
        when(mapper.toProductListItemView(any())).thenAnswer(i -> i.getArgument(0));

        service.getProductList(1L, "newest");

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findProductList(eq(1L), captor.capture());
        assertEquals(0, captor.getValue().getPageNumber());
    }
}
