package vn.devpro.javaweb32.dto.customer.product;

import vn.devpro.javaweb32.entity.product.Product;

import javax.persistence.*;
import java.math.BigDecimal;

public class ProductVariantDto {
    @JoinColumn(table = "product_id")
    private Product product;

    private Long id;
    private BigDecimal price;
    private String size;
    private Integer stock;
    private String colorName;

    public ProductVariantDto() {
    }

    public ProductVariantDto(Product product, Long id, BigDecimal price, String size, Integer stock, String colorName) {
        this.product = product;
        this.id = id;
        this.price = price;
        this.size = size;
        this.stock = stock;
        this.colorName = colorName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}
