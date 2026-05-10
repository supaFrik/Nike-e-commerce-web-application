package vn.demo.nike.features.catalog.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.demo.nike.features.catalog.product.dto.response.ProductDetailResponse;
import vn.demo.nike.features.catalog.product.dto.response.ProductQueryResponseMapper;
import vn.demo.nike.features.catalog.product.entity.Product;
import vn.demo.nike.features.catalog.product.exception.ProductNotFoundException;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductRepository productRepository;
    private final ProductQueryResponseMapper productQueryResponseMapper;

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(Long id) {
        Product product = productRepository.findDetailById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.getColors().forEach(color -> {
            color.getImages().size();
            color.getVariants().size();
        });
        return productQueryResponseMapper.toProductDetailResponse(product);
    }
}
