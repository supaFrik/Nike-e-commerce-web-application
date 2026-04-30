package vn.demo.nike.features.catalog.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.catalog.product.domain.Product;
import vn.demo.nike.features.catalog.product.domain.ProductColor;
import vn.demo.nike.features.catalog.product.domain.ProductImage;
import vn.demo.nike.features.catalog.product.request.*;
import vn.demo.nike.features.catalog.product.exception.ProductNotFoundException;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;
import vn.demo.nike.shared.util.ProductImageUrlResolver;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductSummaryResponse> getAllProductSummaries() {
        return productRepository.findAll().stream()
                .map(product -> new ProductSummaryResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getCategory() != null ? product.getCategory().getName() : "Uncategorized",
                        resolveThumbnail(product)
                ))
                .toList();
    }

    public ProductDetailResponse getProductDetail(Long id) {
        return productRepository.findById(id)
                .map(product -> new ProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getSalePrice(),
                product.getDescription(),
                product.getCategory() != null ? product.getCategory().getName() : "Uncategorized",
                product.getProductStatus() != null ? product.getProductStatus().name() : null,
                mapColors(product)
        ))
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private List<ProductColorDetailResponse> mapColors(Product product) {
        if(product.getColors() == null){
            return List.of();
        }

        return product.getColors().stream()
                .sorted(Comparator.comparing(
                        ProductColor::getDisplayOrder,
                        Comparator.nullsLast(Integer::compareTo)
                ))
                .map(color -> new ProductColorDetailResponse(
                        color.getId(),
                        color.getColorName(),
                        color.getHexCode(),
                        color.getDisplayOrder(),
                        mapImages(color),
                        mapVariants(color)
                ))
                .toList();
    }

    private List<ProductImageDetailResponse> mapImages(ProductColor color) {
        if(color.getImages() == null){
            return List.of();
        }
        return color.getImages().stream().map(
                images -> new ProductImageDetailResponse(
                        images.getId(),
                        ProductImageUrlResolver.toPublicUrl(images.getPath()),
                        images.getTitle(),
                        images.getAltText(),
                        images.getIsMainForColor(),
                        images.getOrderIndex()
                )
        ).toList();
    }

    private List<ProductVariantDetailResponse> mapVariants(ProductColor color) {
        if(color.getVariants() == null){
            return List.of();
        }
        return color.getVariants().stream().map(
                variants -> new ProductVariantDetailResponse(
                        variants.getId(),
                        variants.getSku(),
                        variants.getSize(),
                        variants.getStock(),
                        variants.getActive()
                )
        ).toList();
    }

    private String resolveThumbnail(Product product) {
        if (product.getColors() == null || product.getColors().isEmpty()) {
            return null;
        }

        return product.getColors().stream()
                .sorted(Comparator.comparing(
                        ProductColor::getDisplayOrder,
                        Comparator.nullsLast(Integer::compareTo)
                ))
                .map(color -> {
                    if (color.getImages() == null || color.getImages().isEmpty()) {
                        return null;
                    }

                    // Main image
                    return color.getImages().stream()
                            .filter(img -> Boolean.TRUE.equals(img.getIsMainForColor()))
                            .findFirst()
                            .map(ProductImage::getPath)
                            .map(ProductImageUrlResolver::toPublicUrl)

                            // Fallback to first image
                            .orElseGet(() ->
                                    color.getImages().stream()
                                            .findFirst()
                                            .map(ProductImage::getPath)
                                            .map(ProductImageUrlResolver::toPublicUrl)
                                            .orElse(null)
                            );
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
