package vn.demo.nike.features.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.demo.nike.features.cart.domain.CartItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser_Id(Long userId);

    Optional<CartItem> findByIdAndUser_Id(Long cartItemId, Long userId);

    Optional<CartItem> findByUser_IdAndVariant_Id(Long userId, Long variantId);

    @Query("""
  SELECT COALESCE(SUM(c.quantity), 0)
  FROM CartItem c
  WHERE c.user.id = :userId
  """)
    int sumQuantityByUser_Id(Long userId);
}
