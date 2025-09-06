package vn.devpro.javaweb32.dto.cart;

import java.math.BigDecimal;

public class ProductCartDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String images;

    public ProductCartDto() {}

    public ProductCartDto(Long id, String name, BigDecimal price, String description, String images) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.images = images;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
