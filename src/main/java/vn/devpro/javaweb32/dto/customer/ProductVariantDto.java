package vn.devpro.javaweb32.dto.customer;

import vn.devpro.javaweb32.entity.product.Product;

import java.math.BigDecimal;

public record ProductVariantDto(Product product, Long id, String size, String color, Integer stock, BigDecimal price) {
}
