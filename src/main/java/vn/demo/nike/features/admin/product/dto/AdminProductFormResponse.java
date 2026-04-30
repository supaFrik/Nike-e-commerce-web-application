package vn.demo.nike.features.admin.product.dto;

import vn.demo.nike.features.catalog.product.domain.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

public record AdminProductFormResponse(
        Long productId,
        String name,
        String description,
        String type,
        Long categoryId,
        ProductStatus productStatus,
        BigDecimal price,
        BigDecimal salePrice,
        List<AdminColorFormItem> colors
) {
    public record AdminColorFormItem(
            String colorName,
            String hexCode,
            Integer displayOrder,
            List<AdminImageFormItem> images,
            List<AdminVariantFormItem> variants
    ) {
    }

    public record AdminImageFormItem(
            Long existingImageId,
            String path,
            String title,
            String altText,
            Integer orderIndex,
            Boolean isMainForColor
    ) {
    }

    public record AdminVariantFormItem(
            String sku,
            String size,
            Integer stock,
            Boolean active
    ) {
    }
}
