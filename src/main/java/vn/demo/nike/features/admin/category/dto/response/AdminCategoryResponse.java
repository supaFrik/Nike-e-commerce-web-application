package vn.demo.nike.features.admin.category.dto.response;

public record AdminCategoryResponse(
        Long id,
        String name,
        String redirectUrl
) {
}
