package vn.demo.nike.features.catalog.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.demo.nike.features.catalog.product.domain.Product;
import vn.demo.nike.features.catalog.product.request.ProductListItemView;
import vn.demo.nike.features.catalog.search.dto.SearchProductProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    default Optional<Product> findDetailById(Long id) {
        return findById(id);
    }

    List<Product> findByCategory_Id(Long id);

    List<Product> findByCategory_Id(Long id, Sort sort);

    @Query("""
    SELECT new vn.demo.nike.features.catalog.product.request.ProductListItemView(
        p.id,
        p.name,
        p.price,
        p.salePrice,
        CASE 
            WHEN p.salePrice IS NOT NULL AND p.salePrice > 0 
            THEN true 
            ELSE false 
        END,
        p.productStatus,
        p.type,
        COALESCE(c.name, 'Uncategorized'),
        (
            SELECT MIN(pi.path)
            FROM ProductImage pi
            WHERE pi.color.product.id = p.id
            AND pi.isMainForColor = true
        ),
        SIZE(p.colors)
    )
    FROM Product p
    LEFT JOIN p.category c
    WHERE (:categoryId IS NULL OR c.id = :categoryId)
""")
    List<ProductListItemView> findProductList(@Param("categoryId") Long categoryId, Pageable pageable);

}
