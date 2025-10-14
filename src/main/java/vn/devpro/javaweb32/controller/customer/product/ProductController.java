package vn.devpro.javaweb32.controller.customer.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import vn.devpro.javaweb32.dto.customer.product.ProductResponseDto;
import vn.devpro.javaweb32.service.customer.CustomerProductService;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer/products")
public class ProductController {

    private final CustomerProductService productService;

    @Autowired
    public ProductController(CustomerProductService productService) {
        this.productService = productService;
    }

    // ---------------------------
    // 1. Lấy tất cả sản phẩm còn đang có
    // ---------------------------
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(@RequestParam(value = "userId", required = false) Long userId) {
        List<ProductResponseDto> products = productService.getAllProducts(userId);
        return ResponseEntity.ok(products);
    }

    // ---------------------------
    // 2. Hiện sản phẩm theo ID (tùy chọn userId)
    // ---------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id,
                                                             @RequestParam(value = "userId", required = false) Long userId) {
        ProductResponseDto product = productService.getProductById(id, userId);
        return ResponseEntity.ok(product);
    }

    // ---------------------------
    // 3. Hiện sản phẩm được yêu thích (tùy chọn userId)
    // ---------------------------
    @GetMapping("/featured")
    public ResponseEntity<List<ProductResponseDto>> getFeaturedProducts(@RequestParam(value = "userId", required = false) Long userId) {
        List<ProductResponseDto> featuredProducts = productService.getFeaturedProducts(userId);
        return ResponseEntity.ok(featuredProducts);
    }

    // ---------------------------
    // 4. Hiện sản phẩm theo danh mục (tùy chọn userId)
    // ---------------------------
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable Long categoryId,
                                                                          @RequestParam(value = "userId", required = false) Long userId) {
        List<ProductResponseDto> products = productService.getProductsByCategory(categoryId, userId);
        return ResponseEntity.ok(products);
    }

    // ---------------------------
    // 5. Thêm tắt sản phẩm được yêu thích
    // ---------------------------
    @PostMapping("/{productId}/favourite")
    public ResponseEntity<Map<String, Object>> toggleFavourite(@PathVariable Long productId,
                                                               @RequestParam Long userId) {
        boolean nowFavourite = productService.toggleFavourite(userId, productId);
        Map<String, Object> body = new HashMap<>();
        body.put("productId", productId);
        body.put("userId", userId);
        body.put("favourite", nowFavourite);
        body.put("message", nowFavourite ? "Product added to favourites" : "Product removed from favourites");
        return ResponseEntity.ok(body);
    }

    // ---------------------------
    // Xử lý ngoại lệ
    // ---------------------------
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EntityNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
