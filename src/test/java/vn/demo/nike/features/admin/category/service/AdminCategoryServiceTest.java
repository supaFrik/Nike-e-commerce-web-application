package vn.demo.nike.features.admin.category.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.demo.nike.features.admin.category.dto.AdminCategoryCreateRequest;
import vn.demo.nike.features.catalog.category.domain.Category;
import vn.demo.nike.features.catalog.category.repository.CategoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminCategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private AdminCategoryService adminCategoryService;

    @Test
    void createCategory_trimsAndPersistsName() {
        when(categoryRepository.existsByNameIgnoreCase("Running")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            category.setId(5L);
            return category;
        });

        var response = adminCategoryService.createCategory(new AdminCategoryCreateRequest("  Running  "));

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryCaptor.capture());
        assertThat(categoryCaptor.getValue().getName()).isEqualTo("Running");
        assertThat(response.id()).isEqualTo(5L);
        assertThat(response.name()).isEqualTo("Running");
        assertThat(response.redirectUrl()).isEqualTo("/admin/category/list");
    }

    @Test
    void createCategory_rejectsDuplicateName() {
        when(categoryRepository.existsByNameIgnoreCase("Running")).thenReturn(true);

        assertThatThrownBy(() -> adminCategoryService.createCategory(new AdminCategoryCreateRequest("Running")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Category name already exists.");
    }
}
