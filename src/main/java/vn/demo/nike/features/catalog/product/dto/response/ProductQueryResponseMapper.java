package vn.demo.nike.features.catalog.product.dto.response;

import org.springframework.stereotype.Component;
import vn.demo.nike.features.catalog.product.dto.request.ProductListItemView;
import vn.demo.nike.features.catalog.product.entity.Product;
import vn.demo.nike.features.catalog.product.entity.ProductColor;
import vn.demo.nike.features.catalog.product.entity.ProductImage;
import vn.demo.nike.features.catalog.product.entity.ProductVariant;
import vn.demo.nike.shared.util.ProductImageUrlResolverUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Mapper class to convert Product domain entities
 * into various response DTOs for API queries.
 */

@Component
public class ProductQueryResponseMapper {

    public ProductDetailResponse toProductDetailResponse(Product product) {
        String categoryName = product.getCategory() != null ? product.getCategory().getName() : "Uncategorized";
        String status = product.getProductStatus() != null ? product.getProductStatus().getDisplayName() : "Unavailable";

        return new ProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getSalePrice(),
                product.getDescription(),
                categoryName,
                status,
                mapColors(product)
        );
    }

    public ProductSummaryResponse toProductSummaryResponse(Product product) {
        return new ProductSummaryResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategory() != null ? product.getCategory().getName() : "Uncategorized",
                resolveThumbnail(product)
        );
    }

    public ProductListItemView toProductListItemView(ProductListItemView product) {
        return new ProductListItemView(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getSalePrice(),
                product.isHasSale(),
                product.getStatus(),
                product.getType(),
                product.getCategoryName(),
                ProductImageUrlResolverUtil.toPublicUrl(product.getHeroImg()),
                product.getColorCount()
        );
    }

    private List<ProductColorDetailResponse> mapColors(Product product) {
        if (product.getColors() == null) {
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
        if (color.getImages() == null) {
            return List.of();
        }

        return color.getImages().stream()
                .sorted(Comparator.comparing(
                        ProductImage::getOrderIndex,
                        Comparator.nullsLast(Integer::compareTo)
                ))
                .map(image -> new ProductImageDetailResponse(
                        image.getId(),
                        ProductImageUrlResolverUtil.toPublicUrl(image.getPath()),
                        image.getTitle(),
                        image.getAltText(),
                        image.getIsMainForColor(),
                        image.getOrderIndex()
                ))
                .toList();
    }

    private List<ProductVariantDetailResponse> mapVariants(ProductColor color) {
        if (color.getVariants() == null) {
            return List.of();
        }

        return color.getVariants().stream()
                .sorted(Comparator.comparing(ProductVariant::getSize))
                .map(variant -> new ProductVariantDetailResponse(
                        variant.getId(),
                        variant.getSku(),
                        variant.getSize(),
                        variant.getStock(),
                        variant.getActive()
                ))
                .toList();
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

                    return color.getImages().stream()
                            .filter(image -> Boolean.TRUE.equals(image.getIsMainForColor()))
                            .findFirst()
                            .map(ProductImage::getPath)
                            .map(ProductImageUrlResolverUtil::toPublicUrl)
                            .orElseGet(() -> color.getImages().stream()
                                    .findFirst()
                                    .map(ProductImage::getPath)
                                    .map(ProductImageUrlResolverUtil::toPublicUrl)
                                    .orElse(null));
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
