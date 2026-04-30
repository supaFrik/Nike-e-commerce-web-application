package vn.demo.nike.features.admin.page.dto;

public record AdminCategoryListItemResponse(
        Long id,
        String name,
        int productCount
) {
}
