package vn.demo.nike.features.admin.page.dto.response;

public record AdminCategoryListItemResponse(
        Long id,
        String name,
        int productCount
) {
}
