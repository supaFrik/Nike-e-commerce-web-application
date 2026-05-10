package vn.demo.nike.features.catalog.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.demo.nike.features.catalog.category.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAllByOrderByNameAsc();
    boolean existsByNameIgnoreCase(String name);
    Optional<Category> findByNameIgnoreCase(String name);
}
