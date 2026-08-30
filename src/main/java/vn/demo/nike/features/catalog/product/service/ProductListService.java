package vn.demo.nike.features.catalog.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.catalog.product.dto.request.ProductListItemView;
import vn.demo.nike.features.catalog.product.dto.response.ProductQueryResponseMapper;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductListService {
    private final ProductRepository productRepository;
    private final ProductQueryResponseMapper productQueryResponseMapper;

    private static final int PAGE_SIZE = 20;

    public Page<ProductListItemView> getProductList(Long categoryId, String sort, int page) {
        int safePage = Math.max(0, page);
        Pageable pageable = PageRequest.of(safePage, PAGE_SIZE, resolveSort(sort));
        return productRepository.findProductList(categoryId, pageable)
                .map(productQueryResponseMapper::toProductListItemView);
    }

    public Page<ProductListItemView> getProductList(Long categoryId, String sort) {
        return getProductList(categoryId, sort, 0);
    }

    private Sort resolveSort(String sort) {
        return switch (sort) {
            case "price_asc" -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "newest" -> Sort.by("createDate").descending();
            default -> Sort.by("createDate").descending();
        };
    }
}
