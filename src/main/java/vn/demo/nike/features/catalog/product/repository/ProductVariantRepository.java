package vn.demo.nike.features.catalog.product.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.demo.nike.features.catalog.product.entity.ProductVariant;

import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT v from ProductVariant v WHERE v.id = :id")
    Optional<ProductVariant> findByIdForUpdate(Long id);
}
