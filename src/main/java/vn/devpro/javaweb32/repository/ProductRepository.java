package vn.devpro.javaweb32.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.devpro.javaweb32.entity.product.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory_Name(String name, Sort sort);
    List<Product> findByCategory_NameIgnoreCase(String name, Sort sort);
    List<Product> findByNameContainingIgnoreCase(String name, Sort sort);

    @EntityGraph(attributePaths = {"variants", "category"})
    Optional<Product> findById(Long id);
}
