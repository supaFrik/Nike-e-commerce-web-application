package vn.devpro.javaweb32.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.devpro.javaweb32.entity.customer.Credential;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {
    @EntityGraph(attributePaths = {"customer"})
    Optional<Credential> findByEmail(String email);
}
