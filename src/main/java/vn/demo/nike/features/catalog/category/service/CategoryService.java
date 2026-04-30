package vn.demo.nike.features.catalog.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.catalog.category.dto.CategoryView;
import vn.demo.nike.features.catalog.category.exception.CategoryNotFoundException;
import vn.demo.nike.features.catalog.category.repository.CategoryRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public List<CategoryView> getAllCategories() {
        return categoryRepository.findAllByOrderByNameAsc().stream().map(c -> new CategoryView(c.getId(), c.getName())).toList();
    }

    public String getCategoryNameById(Long id) {
        if (id == null)  {
            return null;
        }
        return categoryRepository.findById(id)
                .map(c -> c.getName())
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
