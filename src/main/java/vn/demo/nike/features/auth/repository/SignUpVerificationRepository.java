package vn.demo.nike.features.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.demo.nike.features.auth.entity.SignupVerification;

import java.util.Optional;

public interface SignUpVerificationRepository extends JpaRepository<SignupVerification, Long> {
    Optional<SignupVerification> findTopByEmailOrderByCreateDateDesc(String email);
}
