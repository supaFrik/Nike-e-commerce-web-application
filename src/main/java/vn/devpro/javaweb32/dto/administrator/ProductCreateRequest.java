package vn.devpro.javaweb32.dto.administrator;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

public class ProductCreateRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Name too long")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description too long")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be > 0")
    private BigDecimal price;

    @DecimalMin(value = "0.00", message = "Sale price must be >= 0")
    private BigDecimal salePrice; // optional override

    @Size(max = 50, message = "Type too long")
    private String type; // MEN / WOMEN / UNISEX

    private Long categoryId;

    @Size(max = 255, message = "SEO slug too long")
    private String seo;

    @Min(value = 0, message = "defaultStock must be >= 0")
    private Integer defaultStock;

    @NotNull(message = "Colors list required")
    @Size(min = 1, message = "At least one color is required")
    private List<ColorData> colors = new ArrayList<>();

    public static class ColorData {
        @NotBlank(message = "Color name required")
        @Size(max = 50, message = "Color name too long")
        private String name;

        @NotNull(message = "Sizes list required")
        @Size(min = 1, message = "At least one size per color")
        private List<@Size(max = 10, message = "Size label too long") String> sizes = new ArrayList<>();

        private List<@Pattern(regexp = "data:.*;base64,.*", message = "Image must be a data URL base64") String> images = new ArrayList<>();

        @Min(value = 0, message = "defaultImageIndex cannot be negative")
        private Integer defaultImageIndex;

        @Min(value = 0, message = "defaultStock must be >= 0")
        private Integer defaultStock;
        private List<@Min(value = 0, message = "Stock must be >= 0") Integer> sizeStocks = new ArrayList<>();

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public List<String> getSizes() { return sizes; }
        public void setSizes(List<String> sizes) { this.sizes = sizes; }
        public List<String> getImages() { return images; }
        public void setImages(List<String> images) { this.images = images; }
        public Integer getDefaultImageIndex() { return defaultImageIndex; }
        public void setDefaultImageIndex(Integer defaultImageIndex) { this.defaultImageIndex = defaultImageIndex; }
        public Integer getDefaultStock() { return defaultStock; }
        public void setDefaultStock(Integer defaultStock) { this.defaultStock = defaultStock; }
        public List<Integer> getSizeStocks() { return sizeStocks; }
        public void setSizeStocks(List<Integer> sizeStocks) { this.sizeStocks = sizeStocks; }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getSeo() { return seo; }
    public void setSeo(String seo) { this.seo = seo; }
    public Integer getDefaultStock() { return defaultStock; }
    public void setDefaultStock(Integer defaultStock) { this.defaultStock = defaultStock; }
    public List<ColorData> getColors() { return colors; }
    public void setColors(List<ColorData> colors) { this.colors = colors; }
}
