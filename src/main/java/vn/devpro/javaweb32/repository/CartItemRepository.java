package vn.devpro.javaweb32.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.devpro.javaweb32.entity.cart.CartItem;
import vn.devpro.javaweb32.entity.customer.Customer;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCustomer(Customer customer);

    CartItem findByCustomerAndProduct_idAndSize(Customer customer, Long id, String size);

    void deleteByCustomerAndProduct_idAndSize(Customer customer, Long id, String size);
}
