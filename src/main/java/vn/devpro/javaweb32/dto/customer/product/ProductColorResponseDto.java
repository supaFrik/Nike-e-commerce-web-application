package vn.devpro.javaweb32.dto.customer.product;

public class ProductColorResponseDto {
    private Long id;
    private String colorName;
    private String hexCode;
    private Boolean active;
    private String swatchUrl;  // đường dẫn ảnh mẫu màu
    private String previewImage; // ảnh hiển thị mặc định trên UI
    private String folderPath;
    private String baseImage;
    private String imageUrl;

    public ProductColorResponseDto(Long id, String colorName, String hexCode, Boolean active, String swatchUrl, String previewImage, String folderPath, String baseImage, String imageUrl) {
        this.id = id;
        this.colorName = colorName;
        this.hexCode = hexCode;
        this.active = active;
        this.swatchUrl = swatchUrl;
        this.previewImage = previewImage;
        this.folderPath = folderPath;
        this.baseImage = baseImage;
        this.imageUrl = imageUrl;
    }

    public ProductColorResponseDto() {
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

    public String getSwatchUrl() {
        return swatchUrl;
    }

    public void setSwatchUrl(String swatchUrl) {
        this.swatchUrl = swatchUrl;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
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
}
