package vn.demo.nike.features.admin.category.dto;

public record AdminCategoryResponse(
        Long id,
        String name,
        String redirectUrl
) {
}
