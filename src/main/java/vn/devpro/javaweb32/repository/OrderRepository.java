package vn.devpro.javaweb32.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.entity.order.Order;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findTop1ByCustomerOrderByCreatedAtDesc(Customer customer);
}

