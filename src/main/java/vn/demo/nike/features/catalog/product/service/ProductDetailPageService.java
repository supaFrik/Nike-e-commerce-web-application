package vn.demo.nike.features.catalog.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.catalog.product.domain.Product;
import vn.demo.nike.features.catalog.product.domain.ProductColor;
import vn.demo.nike.features.catalog.product.domain.ProductImage;
import vn.demo.nike.features.catalog.product.domain.ProductVariant;
import vn.demo.nike.features.catalog.product.exception.ProductNotFoundException;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;
import vn.demo.nike.features.catalog.product.request.ProductColorDetailResponse;
import vn.demo.nike.features.catalog.product.request.ProductDetailResponse;
import vn.demo.nike.features.catalog.product.request.ProductImageDetailResponse;
import vn.demo.nike.features.catalog.product.request.ProductVariantDetailResponse;
import vn.demo.nike.shared.util.ProductImageUrlResolver;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailPageService {

    private final ProductRepository productRepository;

    public ProductDetailResponse getProductDetailPage(Long id) {
        Product product = findProductOrThrow(id);

        /*
         * Map Product -> ProductDetailResponse.
         *
         * Required fields:
         * - id
         * - name
         * - price
         * - salePrice
         * - description
         * - categoryName
         * - productStatus
         * - colors
         *
         * Validation:
         * - category can be null, so handle fallback safely.
         * - productStatus should be converted to String for the response.
         * - colors must come from mapColors(product).
         */
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

    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private List<ProductColorDetailResponse> mapColors(Product product) {
        /*
         * Map product.getColors() -> List<ProductColorDetailResponse>
         *
         * Required behavior:
         * - if colors is null, return List.of()
         * - sort by displayOrder ascending, nulls last
         * - for each color, map:
         *   - id
         *   - colorName
         *   - hexCode
         *   - displayOrder
         *   - images = mapImages(color)
         *   - variants = mapVariants(color)
         */
        if (product.getColors() == null) {
            return List.of();
        }

        return product.getColors().stream()
                .sorted(Comparator.comparing(
                        ProductColor::getDisplayOrder,
                        Comparator.nullsLast(Integer::compareTo)
                ))
                .map(colors -> new ProductColorDetailResponse(
                        colors.getId(),
                        colors.getColorName(),
                        colors.getHexCode(),
                        colors.getDisplayOrder(),
                        mapImages(colors),
                        mapVariants(colors)
                ))
                .toList();
    }

    private List<ProductImageDetailResponse> mapImages(ProductColor color) {
        /*
         * Map color.getImages() -> List<ProductImageDetailResponse>
         *
         * Required behavior:
         * - if images is null, return List.of()
         * - map each image field:
         *   - id
         *   - path
         *   - title
         *   - altText
         *   - isMainForColor
         *   - orderIndex
         *
         * Later improvement:
         * - sort images by orderIndex ascending, nulls last
         */
        if (color.getImages() == null) {
            return List.of();
        }

        return color.getImages().stream()
                .sorted(Comparator.comparing(
                        ProductImage::getOrderIndex,
                        Comparator.nullsLast(Integer::compareTo)
                ))
                .map(
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
        /*
         * Map color.getVariants() -> List<ProductVariantDetailResponse>
         *
         * Required behavior:
         * - if variants is null, return List.of()
         * - map each variant field:
         *   - id
         *   - sku
         *   - size
         *   - stock
         *   - active
         *
         * Later improvement:
         * - sort variants by size if your UI needs stable ordering
         */
        if (color.getVariants() == null) {
            return List.of();
        }
        return color.getVariants().stream()
                .sorted(Comparator.comparing(ProductVariant::getSize))
                .map(
                        variants -> new ProductVariantDetailResponse(
                                variants.getId(),
                                variants.getSku(),
                                variants.getSize(),
                                variants.getStock(),
                                variants.getActive()
                        )
                ).toList();
    }
}
