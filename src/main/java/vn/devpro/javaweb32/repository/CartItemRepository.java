package vn.devpro.javaweb32.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.devpro.javaweb32.entity.cart.CartItem;
import vn.devpro.javaweb32.entity.customer.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCustomer(@Param("customer") Customer customer);

    Optional<CartItem> findByCustomerAndProduct_idAndSizeAndColor(Customer customer, Long id, String size, String color);

    void deleteByCustomerAndProduct_idAndSizeAndColor(Customer customer, Long id, String size, String color);
}
