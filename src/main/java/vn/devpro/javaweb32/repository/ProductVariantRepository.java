package vn.devpro.javaweb32.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductColor;
import vn.devpro.javaweb32.entity.product.ProductVariant;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    @EntityGraph(attributePaths = {"color", "product"})
    List<ProductVariant> findByProductId(Long productId);

    @EntityGraph(attributePaths = {"color", "product"})
    List<ProductVariant> findByProduct(Product product);

    // NOTE: use "sizeLabel" to match entity field name. If your entity still uses "size", revert to "AndSize".
    @EntityGraph(attributePaths = {"color", "product"})
    Optional<ProductVariant> findByProductIdAndColor_IdAndSizeLabel(Long productId, Long colorId, String sizeLabel);

    @EntityGraph(attributePaths = {"color"})
    List<ProductVariant> findByProductIdAndColor_Id(Long productId, Long colorId);

    // Convenience sorted by sizeLabel; if you have numeric sizeValue, consider ordering by sizeValue instead.
    @EntityGraph(attributePaths = {"color"})
    List<ProductVariant> findByProductIdAndColor_IdOrderBySizeLabelAsc(Long productId, Long colorId);

    @Query("select distinct v.color from ProductVariant v where v.product.id = :productId")
    List<ProductColor> findDistinctColorsByProductId(@Param("productId") Long productId);

    @Query("select distinct v.sizeLabel from ProductVariant v " +
            "where v.product.id = :productId and v.color.id = :colorId and coalesce(v.stock,0) > 0 " +
            "order by v.sizeLabel")
    List<String> findAvailableSizesByProductIdAndColorId(@Param("productId") Long productId,
                                                         @Param("colorId") Long colorId);

    List<ProductVariant> findByProductIdAndStockGreaterThan(Long productId, Integer stockThreshold);

    // Useful helper to check duplicates before insert
    boolean existsByProductIdAndColor_IdAndSizeLabel(Long productId, Long colorId, String sizeLabel);
}
