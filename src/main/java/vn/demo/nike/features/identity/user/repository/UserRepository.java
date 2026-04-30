package vn.demo.nike.features.identity.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.demo.nike.features.identity.user.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);

    boolean existsByEmail(String email);

//    Optional<User> findByIdAnd
}
