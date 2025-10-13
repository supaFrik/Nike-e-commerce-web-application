package vn.devpro.javaweb32.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.devpro.javaweb32.dto.customer.product.ProductResponseDto;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.UserFavourite;
import vn.devpro.javaweb32.entity.product.enums.ProductStatus;
import vn.devpro.javaweb32.repository.ProductRepository;
import vn.devpro.javaweb32.repository.UserFavouriteRepository;
import vn.devpro.javaweb32.service.mapper.CustomerProductMapper;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerProductMapper customerProductMapper;

    @Autowired
    private UserFavouriteRepository favouriteRepository;

    /**
     * Lấy toàn bộ sản phẩm đang ở trạng thái ACTIVE
     */
    public List<ProductResponseDto> getAllProducts() {
        return internalGetAllProducts(null);
    }

    public List<ProductResponseDto> getAllProducts(Long userId) {
        return internalGetAllProducts(userId);
    }

    private List<ProductResponseDto> internalGetAllProducts(Long userId) {
        List<Product> products = productRepository.findAllByProductStatus(ProductStatus.ACTIVE, Sort.by("createDate").descending());
        return products.stream()
                .map(product -> {
                    ProductResponseDto dto = customerProductMapper.toProductResponse(product);
                    if (userId != null) {
                        dto.setFavourite(favouriteRepository.existsByUserIdAndProductId(userId, product.getId()));
                    } else {
                        dto.setFavourite(false);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Lấy chi tiết sản phẩm theo ID (cos kiểm tra yêu thích nếu có user)
     */
    public ProductResponseDto getProductById(Long id) {
        return getProductById(id, null);
    }

    public ProductResponseDto getProductById(Long id, Long userId) {
        Product product = productRepository.findByIdAndProductStatus(id, ProductStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm"));

        ProductResponseDto dto = customerProductMapper.toProductResponse(product);
        if (userId != null) {
            dto.setFavourite(favouriteRepository.existsByUserIdAndProductId(userId, id));
        }
        return dto;
    }

    /**
     * Lọc sản phẩm theo danh mục
     */
    public List<ProductResponseDto> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategory_IdAndProductStatus(categoryId, ProductStatus.ACTIVE, Sort.by("createDate").descending());
        return products.stream()
                .map(customerProductMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lọc sản phẩm theo danh mục (có tính năng yêu thích)
     */
    public List<ProductResponseDto> getProductsByCategory(Long categoryId, Long userId) {
        List<ProductResponseDto> list = getProductsByCategory(categoryId);
        if (userId != null) {
            list.forEach(dto -> dto.setFavourite(favouriteRepository.existsByUserIdAndProductId(userId, dto.getId())));
        }
        return list;
    }

    /**
     * Lấy top 8 sản phẩm nổi bật / mới nhất
     */
    public List<ProductResponseDto> getFeaturedProducts() {
        List<Product> products = productRepository.findTop8ByProductStatusOrderByCreateDateDesc(ProductStatus.ACTIVE);
        return products.stream()
                .map(customerProductMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy top 8 sản phẩm nổi bật / mới nhất (có tính năng yêu thích)
     */
    public List<ProductResponseDto> getFeaturedProducts(Long userId) {
        List<ProductResponseDto> list = getFeaturedProducts();
        if (userId != null) {
            list.forEach(dto -> dto.setFavourite(favouriteRepository.existsByUserIdAndProductId(userId, dto.getId())));
        }
        return list;
    }

    /**
     * Chuyển đổi trạng thái yêu thích của sản phẩm
     *
     * @param userId    ID của người dùng
     * @param productId ID của sản phẩm
     * @return trạng thái mới của sản phẩm (true nếu đã được đánh dấu yêu thích)
     */
    public boolean toggleFavourite(Long userId, Long productId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId is required to toggle favourite");
        }
        Product product = productRepository.findByIdAndProductStatus(productId, ProductStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tồn tại hoặc không hoạt động"));

        var existing = favouriteRepository.findByUserIdAndProductId(userId, productId);
        if (existing.isPresent()) {
            favouriteRepository.delete(existing.get());
            return false; // đã xóa yêu thích
        } else {
            favouriteRepository.save(new UserFavourite(userId, product));
            return true; // đã thêm yêu thích
        }
    }
}
