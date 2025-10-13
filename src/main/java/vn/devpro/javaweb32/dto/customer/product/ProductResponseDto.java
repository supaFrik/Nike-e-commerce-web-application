package vn.devpro.javaweb32.dto.customer.product;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ProductResponseDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal salePrice;
    private String description;
    private String status;
    private String type;
    private boolean favourite;
    private String avatarUrl;
    private String seo;
    private Date createDate;
    private String categoryName;
    private List<ProductColorResponseDto> colors;
    private List<ProductVariantResponseDto> variants;
    private List<ProductImageResponseDto> images;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSeo() {
        return seo;
    }

    public void setSeo(String seo) {
        this.seo = seo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ProductColorResponseDto> getColors() {
        return colors;
    }

    public void setColors(List<ProductColorResponseDto> colors) {
        this.colors = colors;
    }

    public List<ProductVariantResponseDto> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariantResponseDto> variants) {
        this.variants = variants;
    }

    public List<ProductImageResponseDto> getImages() {
        return images;
    }

    public void setImages(List<ProductImageResponseDto> images) {
        this.images = images;
    }

    public String getFirstImageUrl() {
        if (images == null || images.isEmpty() || images.get(0) == null) return null;
        return images.get(0).getUrl();
    }
}
