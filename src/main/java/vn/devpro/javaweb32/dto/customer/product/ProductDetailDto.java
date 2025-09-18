package vn.devpro.javaweb32.dto.customer.product;

import vn.devpro.javaweb32.entity.product.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class ProductDetailDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String status;
    private boolean favourites;
    private Date createDate;
    private List<ProductVariantDto> variants;
    private List<ProductImageDto> images;
    private List<ProductColorDto> colors;
    private Category category;

    public ProductDetailDto(Long id, String name, BigDecimal price, String description, String status, boolean favourites, Date createDate, List<ProductVariantDto> variants, List<ProductImageDto> images, List<ProductColorDto> colors , Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.status = status;
        this.favourites = favourites;
        this.createDate = createDate;
        this.variants = variants;
        this.images = images;
        this.colors = colors;
        this.category = category;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public boolean isFavourites() { return favourites; }
    public Date getCreated() { return createDate; }
    public List<ProductVariantDto> getVariants() { return variants; }
    public List<ProductImageDto> getImages() { return images; }
    public List<ProductColorDto> getColors() { return colors; }
    public Category getCategory() { return category; }
}
