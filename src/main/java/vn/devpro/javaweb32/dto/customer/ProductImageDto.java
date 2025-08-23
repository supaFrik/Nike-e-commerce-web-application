package vn.devpro.javaweb32.dto.customer;

import vn.devpro.javaweb32.entity.product.Product;

public record ProductImageDto(Long id, String url, Product product) {}
