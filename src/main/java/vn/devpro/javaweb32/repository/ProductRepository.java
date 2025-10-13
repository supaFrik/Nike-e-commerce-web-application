package vn.devpro.javaweb32.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.enums.ProductStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByProductStatus(ProductStatus productStatus, Sort sort);

    Optional<Product> findByIdAndProductStatus(Long id, ProductStatus productStatus);

    List<Product> findByCategory_IdAndProductStatus(Long categoryId, ProductStatus productStatus, Sort sort);

    List<Product> findByNameContainingIgnoreCase(String name, Sort sort);

    List<Product> findByCategory_NameIgnoreCase(String categoryName, Sort sort);

    Optional<Product> findBySeo(String seo);

    List<Product> findTop8ByProductStatusOrderByCreateDateDesc(ProductStatus productStatus);

    boolean existsByNameIgnoreCase(String name);
}
