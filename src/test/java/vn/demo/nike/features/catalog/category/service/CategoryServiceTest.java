package vn.demo.nike.features.catalog.category.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.demo.nike.features.catalog.category.dto.response.CategoryView;
import vn.demo.nike.features.catalog.category.entity.Category;
import vn.demo.nike.features.catalog.category.exception.CategoryNotFoundException;
import vn.demo.nike.features.catalog.category.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock private CategoryRepository categoryRepository;
    @InjectMocks private CategoryService categoryService;

    private Category cat(long id, String name) {
        Category c = new Category();
        c.setId(id);
        c.setName(name);
        return c;
    }

    @Test
    void getAllCategories_mapsAllOrdered() {
        when(categoryRepository.findAllByOrderByNameAsc()).thenReturn(List.of(cat(2, "B"), cat(1, "A")));

        List<CategoryView> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getId());
        assertEquals("B", result.get(0).getName());
        verify(categoryRepository).findAllByOrderByNameAsc();
    }

    @Test
    void getCategoryNameById_returnsNullWhenIdNull() {
        assertNull(categoryService.getCategoryNameById(null));
        verify(categoryRepository, never()).findById(any());
    }

    @Test
    void getCategoryNameById_returnsNameWhenFound() {
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(cat(10, "Running")));

        assertEquals("Running", categoryService.getCategoryNameById(10L));
    }

    @Test
    void getCategoryNameById_throwsWhenNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryNameById(99L));
    }

    @Test
    void getCategoryIdByName_returnsNullWhenNameNull() {
        assertNull(categoryService.getCategoryIdByName(null));
        verify(categoryRepository, never()).findByNameIgnoreCase(any());
    }

    @Test
    void getCategoryIdByName_returnsIdWhenFound() {
        when(categoryRepository.findByNameIgnoreCase("running")).thenReturn(Optional.of(cat(5, "Running")));

        assertEquals(5L, categoryService.getCategoryIdByName("running"));
    }

    @Test
    void getCategoryIdByName_throwsWhenNotFound() {
        when(categoryRepository.findByNameIgnoreCase("Nope")).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryIdByName("Nope"));
    }
}
