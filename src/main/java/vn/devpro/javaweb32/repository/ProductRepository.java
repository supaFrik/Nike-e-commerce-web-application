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

    @EntityGraph(attributePaths = {"images", "category"})
    List<Product> findByCategory_NameIgnoreCase(String name, Sort sort);

    @EntityGraph(attributePaths = {"images", "category"})
    List<Product> findByNameContainingIgnoreCase(String name, Sort sort);

    @EntityGraph(attributePaths = {"images", "category"})
    Optional<Product> findById(Long id);
}
