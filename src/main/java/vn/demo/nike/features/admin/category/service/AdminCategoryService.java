package vn.demo.nike.features.admin.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.demo.nike.features.admin.category.dto.AdminCategoryCreateRequest;
import vn.demo.nike.features.admin.category.dto.AdminCategoryResponse;
import vn.demo.nike.features.catalog.category.domain.Category;
import vn.demo.nike.features.catalog.category.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public AdminCategoryResponse createCategory(AdminCategoryCreateRequest request) {
        String normalizedName = normalizeName(request.name());

        if (categoryRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new IllegalArgumentException("Category name already exists.");
        }

        Category savedCategory = categoryRepository.save(Category.builder()
                .name(normalizedName)
                .build());

        return new AdminCategoryResponse(savedCategory.getId(), savedCategory.getName(), "/admin/category/list");
    }

    private String normalizeName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Category name is required.");
        }

        String normalized = name.trim().replaceAll("\\s+", " ");
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Category name is required.");
        }
        return normalized;
    }
}
