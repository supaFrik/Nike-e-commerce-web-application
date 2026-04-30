package vn.demo.nike.features.order.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.demo.nike.features.order.domain.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "items")
    Optional<Order> findTopByUser_IdOrderByCreateDateDesc(Long userId);

    @EntityGraph(attributePaths = "items")
    Optional<Order> findByIdAndUser_Id(Long orderId, Long userId);
}
