package vn.devpro.javaweb32.dto.product;

import vn.devpro.javaweb32.entity.product.Product;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ProductColorDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String colorName;
    private String folderPath;
    private String imageUrl;

    public ProductColorDto() {
        super();
    }

    public ProductColorDto(Long id, String colorName, String folderPath, String imageUrl) {
        this.id = id;
        this.colorName = colorName;
        this.folderPath = folderPath;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
