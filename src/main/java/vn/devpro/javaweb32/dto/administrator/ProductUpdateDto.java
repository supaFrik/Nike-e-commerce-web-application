package vn.devpro.javaweb32.dto.administrator;

import java.math.BigDecimal;
import java.util.List;

public class ProductUpdateDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal salePrice;
    private String description;
    private String status;
    private Long categoryId;
    private String seo;

    private List<ProductColorInputDto> colors;
    private List<ProductVariantInputDto> variants;

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
