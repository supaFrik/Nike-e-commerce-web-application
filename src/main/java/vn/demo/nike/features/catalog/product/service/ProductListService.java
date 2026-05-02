package vn.demo.nike.features.catalog.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;
import vn.demo.nike.features.catalog.product.request.ProductListItemView;
import vn.demo.nike.shared.util.ProductImageUrlResolver;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductListService {
    private final ProductRepository productRepository;

    public List<ProductListItemView> getAllProductItems(Long categoryId, String sort) {
        Pageable pageable = PageRequest.of(0, 100, resolveSort(sort));
        return productRepository.findProductList(categoryId, pageable).stream()
                .map(product -> new ProductListItemView(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getSalePrice(),
                        product.isHasSale(),
                        product.getStatus(),
                        product.getType(),
                        product.getCategoryName(),
                        ProductImageUrlResolver.toPublicUrl(product.getHeroImg()),
                        product.getColorCount()
                ))
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
