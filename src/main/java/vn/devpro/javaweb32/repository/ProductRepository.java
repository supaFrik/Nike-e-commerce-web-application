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

    // ==== 1. Basic cơ bản ====
    @EntityGraph(attributePaths = {"images", "category"})
    List<Product> findByCategory_NameIgnoreCase(String name, Sort sort);

    @EntityGraph(attributePaths = {"images", "category"})
    List<Product> findByNameContainingIgnoreCase(String name, Sort sort);

    @EntityGraph(attributePaths = {"images", "category"})
    Optional<Product> findById(Long id);

    // ==== 2. Lọc theo ID sản phẩm ====
    @EntityGraph(attributePaths = {"images", "category"})
    List<Product> findByCategory_Id(Long categoryId, Sort sort);

    // ==== 3. Lọc theo status ====
    @EntityGraph(attributePaths = {"images", "category"})
    List<Product> findByStatusIgnoreCase(String status, Sort sort);

    // ==== 4. Chi tiết sản phẩm theo màu ====
    @EntityGraph(attributePaths = {"images", "category", "variants", "variants.color"})
    Optional<Product> findByIdWithVariants(Long id);

    // ==== 5. Lọc theo SEO slug ====
    @EntityGraph(attributePaths = {"images", "category", "variants"})
    Optional<Product> findBySeo(String seo);

    // ==== 6. Kiểm tra theo tên ====
    boolean existsByNameIgnoreCase(String name);

    // ==== 7. Sản phẩm hot ở trang chính ====
    @EntityGraph(attributePaths = {"images", "category"})
    List<Product> findTop10ByStatusOrderByUpdateDateDesc(String status);
}
