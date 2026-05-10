package vn.demo.nike.features.catalog.category.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.demo.nike.features.catalog.category.entity.Category;
import vn.demo.nike.features.catalog.category.exception.CategoryNotFoundException;
import vn.demo.nike.features.catalog.category.repository.CategoryRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Test
    void getCategoryIdByName_returnsMatchingCategoryId() {
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(categoryRepository);
        Category category = Category.builder().name("Running").build();
        category.setId(7L);

        when(categoryRepository.findByNameIgnoreCase("Running")).thenReturn(Optional.of(category));

        Long categoryId = categoryService.getCategoryIdByName("Running");

        assertThat(categoryId).isEqualTo(7L);
    }

    @Test
    void getCategoryIdByName_throwsWhenCategoryDoesNotExist() {
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(categoryRepository);

        when(categoryRepository.findByNameIgnoreCase("Running")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryIdByName("Running"))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("Category with name - Running not found");
    }
}
