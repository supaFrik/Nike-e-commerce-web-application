package vn.devpro.javaweb32.entity.product;

import vn.devpro.javaweb32.common.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "product_colors")
public class ProductColor extends BaseEntity {

    @Column(name = "color_name", length = 100, nullable = false)
    private String colorName;

    @Column(name = "hex_code", length = 7)
    private String hexCode;

    @Column(name = "swatch_path", length = 1024)
    private String swatchPath;

    @Column(name = "folder_path", length = 150, nullable = false)
    private String folderPath;

    @Column(name = "base_image", length = 150, nullable = false)
    private String baseImage;

    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductColor() {}

    public ProductColor(String colorName, String hexCode) {
        this.colorName = colorName;
        this.hexCode = hexCode;
    }

    @PrePersist
    @PreUpdate
    private void ensureSlugs() {
        if (colorName == null) {
            colorName = "Unknown"; // guard
        }
        if (folderPath == null || folderPath.isBlank()) {
            folderPath = slugify(colorName);
        }
        if (product != null && product.getName() != null) {
            if (baseImage == null || baseImage.isBlank()) {
                baseImage = slugify(product.getName()) + "-" + slugify(colorName);
            }
        } else if (baseImage == null || baseImage.isBlank()) {
            baseImage = slugify(colorName);
        }
        if (folderPath.length() > 150) folderPath = folderPath.substring(0, 150);
        if (baseImage.length() > 150) baseImage = baseImage.substring(0, 150);
    }

    private String slugify(String input) {
        if (input == null) return "n-a";
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        String slug = normalized.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+", "")
                .replaceAll("-+$", "");
        return slug.isBlank() ? "n-a" : slug;
    }

    public Long getId() { return super.getId(); }

    public String getColorName() { return colorName; }
    public void setColorName(String colorName) { this.colorName = colorName; }

    public String getHexCode() { return hexCode; }
    public void setHexCode(String hexCode) { this.hexCode = hexCode; }

    public String getSwatchPath() { return swatchPath; }
    public void setSwatchPath(String swatchPath) { this.swatchPath = swatchPath; }

    public String getFolderPath() { return folderPath; }
    public void setFolderPath(String folderPath) { this.folderPath = folderPath; }

    public String getBaseImage() { return baseImage; }
    public void setBaseImage(String baseImage) { this.baseImage = baseImage; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    @Transient
    public String getImageUrl() {
        if (swatchPath != null) return swatchPath;
        if (product != null && baseImage != null && folderPath != null && product.getName() != null) {
            return "/images/products/" + product.getName() + "/" + folderPath + "/" + baseImage + "-1.avif";
        }
        return null;
    }
}
