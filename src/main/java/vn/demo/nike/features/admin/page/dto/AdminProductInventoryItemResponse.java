package vn.demo.nike.features.admin.page.dto;

import java.math.BigDecimal;
import java.util.List;

public record AdminProductInventoryItemResponse(
        Long id,
        String name,
        String description,
        String categoryName,
        String statusLabel,
        String imageUrl,
        BigDecimal price,
        BigDecimal salePrice,
        int stock,
        List<String> colorNames,
        List<String> sizes
) {
}
