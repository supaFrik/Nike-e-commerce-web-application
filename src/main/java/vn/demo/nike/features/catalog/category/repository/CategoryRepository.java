package vn.demo.nike.features.catalog.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.demo.nike.features.catalog.category.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAllByOrderByNameAsc();
    boolean existsByNameIgnoreCase(String name);
}
