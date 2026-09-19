package vn.demo.nike.features.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.demo.nike.features.coupon.entity.Coupon;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCodeIgnoreCase(String code);
    Boolean existsByCode(String code);
    List<Coupon> findByActiveTrue();

    Object findByCode(String code);
}
