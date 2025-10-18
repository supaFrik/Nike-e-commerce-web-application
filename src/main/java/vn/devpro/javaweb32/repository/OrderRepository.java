package vn.devpro.javaweb32.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.entity.order.Order;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findTop1ByCustomerOrderByCreatedAtDesc(Customer customer);

    @Query("select distinct o from Order o " +
            "join fetch o.customer c " +
            "left join fetch c.credential cred " +
            "left join fetch c.addresses " +
            "where o.id = :id")
    Optional<Order> findByIdWithCustomerAndAddresses(@Param("id") Long id);

    @Query("""
            select distinct o from Order o
            left join fetch o.items i
            left join fetch i.product p
            where o.id = :id
            """)
    Optional<Order> findByIdWithItems(@Param("id") Long id);
}
