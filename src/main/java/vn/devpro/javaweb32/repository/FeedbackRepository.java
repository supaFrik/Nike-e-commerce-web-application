package vn.devpro.javaweb32.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.devpro.javaweb32.entity.customer.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
