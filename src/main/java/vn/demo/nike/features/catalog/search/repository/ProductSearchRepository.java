package vn.demo.nike.features.catalog.search.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import vn.demo.nike.features.catalog.product.domain.Product;
import vn.demo.nike.features.catalog.search.dto.SearchProductProjection;

public interface ProductSearchRepository extends Repository<Product, Long> {
    @Query(value = """
    SELECT 
        p.id as id,
        p.name as name,
        p.price as price,
        p.salePrice as salePrice,
        c.name as categoryName,
        (
            SELECT MIN(pi.path)
            FROM ProductImage pi
            WHERE pi.color.product.id = p.id
            AND pi.isMainForColor = true
        ) as imageUrl
    FROM Product p
    JOIN p.category c
    WHERE 
        (:query IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))
         OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
         OR LOWER(p.type) LIKE LOWER(CONCAT('%', :query, '%')))
    AND 
        (:category IS NULL OR LOWER(c.name) = LOWER(:category))
""",
            countQuery = """
    SELECT COUNT(p.id)
    FROM Product p
    JOIN p.category c
    WHERE
        (:query IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))
         OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
         OR LOWER(p.type) LIKE LOWER(CONCAT('%', :query, '%')))
    AND
        (:category IS NULL OR LOWER(c.name) = LOWER(:category))
""")
    Page<SearchProductProjection> search(
            @Param("query") String query,
            @Param("category") String category,
            Pageable pageable
    );
}
