package vn.demo.nike.features.catalog.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.catalog.product.dto.request.ProductListItemView;
import vn.demo.nike.features.catalog.product.dto.response.ProductCartResponse;
import vn.demo.nike.features.catalog.product.dto.response.ProductQueryResponseMapper;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;

import java.util.List;

/**
 * Provide queries related to product listing,
 * such as fetching products by category with sorting options.
 */

@Service
@RequiredArgsConstructor
public class ProductListService {
    private final ProductRepository productRepository;
    private final ProductQueryResponseMapper productQueryResponseMapper;

    public List<ProductListItemView> getProductList(Long categoryId, String sort) {
        Pageable pageable = PageRequest.of(0, 100, resolveSort(sort));
        return productRepository.findProductList(categoryId, pageable).stream()
                .map(productQueryResponseMapper::toProductListItemView)
                .toList();
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
