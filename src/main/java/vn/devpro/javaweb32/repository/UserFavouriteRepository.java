package vn.devpro.javaweb32.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.devpro.javaweb32.entity.product.UserFavourite;
import java.util.Optional;
import java.util.List;

public interface UserFavouriteRepository extends JpaRepository<UserFavourite, Long> {
    Optional<UserFavourite> findByUserIdAndProductId(Long userId, Long productId);
    List<UserFavourite> findByUserId(Long userId);
    long countByProductId(Long productId);
}
