package vn.devpro.javaweb32.dto.administrator;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public class ProductCreateDto {
    private String name;
    private BigDecimal price;
    private BigDecimal salePrice;
    private String description;
    private String type;
    private Long categoryId;
    private String seo;
    private String status;

    // Upload ảnh chính
    private MultipartFile avatar;

    // Các màu và biến thể
    private List<ProductColorInputDto> colors;
    private List<ProductVariantInputDto> variants;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getSeo() {
        return seo;
    }

    public void setSeo(String seo) {
        this.seo = seo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }

    public List<ProductColorInputDto> getColors() {
        return colors;
    }

    public void setColors(List<ProductColorInputDto> colors) {
        this.colors = colors;
    }

    public List<ProductVariantInputDto> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariantInputDto> variants) {
        this.variants = variants;
    }
}
