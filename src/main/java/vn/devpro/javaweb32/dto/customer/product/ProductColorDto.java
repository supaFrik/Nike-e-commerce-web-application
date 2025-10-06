package vn.devpro.javaweb32.dto.customer.product;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ProductColorDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String colorName;
    private String folderPath;
    private String baseImage;
    private String imageUrl;
    private boolean defaultImage;

    public ProductColorDto() {
        super();
    }

    public ProductColorDto(Long id, String colorName, String folderPath, String baseImage, String imageUrl, boolean defaultImage) {
        this.id = id;
        this.colorName = colorName;
        this.folderPath = folderPath;
        this.baseImage = baseImage;
        this.imageUrl = imageUrl;
        this.defaultImage = defaultImage;
    }

    public ProductColorDto(Long id, String colorName, String folderPath) {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getBaseImage() {
        return baseImage;
    }

    public void setBaseImage(String baseImage) {
        this.baseImage = baseImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(boolean defaultImage) {
        this.defaultImage = defaultImage;
    }
}
