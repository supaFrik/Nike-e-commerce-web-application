package vn.devpro.javaweb32.dto.customer.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal salePrice;
    private String description;
    private String type;
    private Long categoryId;
    private String status;
    private String seo;
    private boolean favourites = true;

    private List<ColorDto> colors = new ArrayList<>();
    private List<VariantDto> variants = new ArrayList<>();

    public static class ColorDto {
        private String colorName;
        private MultipartFile[] images;
        private boolean defaultPreview;
        private String existingPreviewFilename;

        public String getColorName() { return colorName; }
        public void setColorName(String colorName) { this.colorName = colorName; }

        public MultipartFile[] getImages() { return images; }
        public void setImages(MultipartFile[] images) { this.images = images; }

        public boolean isDefaultPreview() { return defaultPreview; }
        public void setDefaultPreview(boolean defaultPreview) { this.defaultPreview = defaultPreview; }

        public String getExistingPreviewFilename() { return existingPreviewFilename; }
        public void setExistingPreviewFilename(String existingPreviewFilename) { this.existingPreviewFilename = existingPreviewFilename; }
    }

    public static class VariantDto {
        private String size;
        private BigDecimal price;
        private Integer stock;
        private Integer colorIndex; // index into colors list

        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public Integer getStock() { return stock; }
        public void setStock(Integer stock) { this.stock = stock; }
        public Integer getColorIndex() { return colorIndex; }
        public void setColorIndex(Integer colorIndex) { this.colorIndex = colorIndex; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSeo() { return seo; }
    public void setSeo(String seo) { this.seo = seo; }
    public boolean isFavourites() { return favourites; }
    public void setFavourites(boolean favourites) { this.favourites = favourites; }
    public List<ColorDto> getColors() {
        if (colors == null) {
            colors = new ArrayList<>();
        }
        return colors;
    }

    public void setColors(List<ColorDto> colors) {
        this.colors = colors != null ? colors : new ArrayList<>();
    }

    public List<VariantDto> getVariants() {
        if (variants == null) {
            variants = new ArrayList<>();
        }
        return variants;
    }

    public void setVariants(List<VariantDto> variants) {
        this.variants = variants != null ? variants : new ArrayList<>();
    }
}
