package vn.demo.nike.features.admin.category.dto;

import jakarta.validation.constraints.NotBlank;

public record AdminCategoryCreateRequest(
        @NotBlank(message = "Category name is required")
        String name
) {
}
