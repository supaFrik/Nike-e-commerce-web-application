package vn.devpro.javaweb32.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.devpro.javaweb32.entity.cart.CartItem;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCustomer_id(Long id);
}
