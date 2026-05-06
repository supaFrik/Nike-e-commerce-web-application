package vn.demo.nike.features.catalog.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.catalog.product.dto.response.ProductDetailResponse;
import vn.demo.nike.features.catalog.product.dto.response.ProductQueryResponseMapper;
import vn.demo.nike.features.catalog.product.exception.ProductNotFoundException;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;

/**
 * Provide queries related to product details
 */

@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductRepository productRepository;
    private final ProductQueryResponseMapper productQueryResponseMapper;

    public ProductDetailResponse getProductDetail(Long id) {
        return productRepository.findById(id)
                .map(productQueryResponseMapper::toProductDetailResponse)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
