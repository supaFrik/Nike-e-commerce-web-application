package vn.devpro.javaweb32.dto.customer;

import vn.devpro.javaweb32.entity.product.Category;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductImage;
import vn.devpro.javaweb32.entity.product.ProductVariant;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public record ProductDetailDto(Long id, String name, BigDecimal price, String description, String status, Integer stock, boolean featured, LocalDateTime createdAt, List<ProductVariant> variants, List<ProductImage> images, Category category) {
}
