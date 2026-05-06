package vn.demo.nike.features.identity.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.demo.nike.features.identity.user.entity.Address;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByIdAndUser_Id(Long id, Long userId);

    Optional<Address> findByUser_IdAndPrimaryAddressTrue(Long userId);
}
