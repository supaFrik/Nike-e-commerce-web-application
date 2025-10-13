package vn.devpro.javaweb32.dto.customer.product;

public class ProductImageResponseDto {
    private Long id;
    private String imageUrl;
    private Long colorId; // ảnh thuộc về màu nào (nếu có)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }

    public String getUrl() {
        return imageUrl;
    }
}
