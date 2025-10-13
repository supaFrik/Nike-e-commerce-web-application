package vn.devpro.javaweb32.dto.administrator;

import org.springframework.web.multipart.MultipartFile;

public class ProductColorInputDto {
    private String colorName;
    private String hexCode;
    private Boolean active;
    private MultipartFile[] images; // upload ảnh cho màu này

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public MultipartFile[] getImages() {
        return images;
    }

    public void setImages(MultipartFile[] images) {
        this.images = images;
    }
}
