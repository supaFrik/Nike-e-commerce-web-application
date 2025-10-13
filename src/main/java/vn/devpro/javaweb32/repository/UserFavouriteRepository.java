package vn.devpro.javaweb32.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.devpro.javaweb32.entity.product.UserFavourite;
import java.util.Optional;
import java.util.List;

public interface UserFavouriteRepository extends JpaRepository<UserFavourite, Long> {
    // Lấy 1 record cụ thể
    Optional<UserFavourite> findByUserIdAndProductId(Long userId, Long productId);

    // Xóa 1 yêu thích
    void deleteByUserIdAndProductId(Long userId, Long productId);

    // Lấy danh sách sản phẩm yêu thích của 1 người
    List<UserFavourite> findByUserId(Long userId);

    // Đếm số yêu thích sản phẩm
    long countByProductId(Long productId);

    // Kiểm tra xem User đã thích chưa
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
