package vn.devpro.javaweb32.dto.product;

import vn.devpro.javaweb32.entity.product.Category;
import vn.devpro.javaweb32.entity.product.ProductImage;
import vn.devpro.javaweb32.entity.product.ProductVariant;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ProductDetailDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String status;
    private boolean favourites;
    private LocalDateTime createdAt;
    private List<ProductVariant> variants;
    private List<ProductImage> images;
    private Category category;

    public ProductDetailDto(Long id, String name, BigDecimal price, String description, String status, boolean favourites, LocalDateTime createdAt, List<ProductVariant> variants, List<ProductImage> images, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.status = status;
        this.favourites = favourites;
        this.createdAt = createdAt;
        this.variants = variants;
        this.images = images;
        this.category = category;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public boolean isFavourites() { return favourites; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<ProductVariant> getVariants() { return variants; }
    public List<ProductImage> getImages() { return images; }
    public Category getCategory() { return category; }
}
