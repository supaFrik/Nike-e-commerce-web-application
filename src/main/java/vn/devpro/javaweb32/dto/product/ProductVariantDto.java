package vn.devpro.javaweb32.dto.product;

import vn.devpro.javaweb32.entity.product.Product;

import javax.persistence.*;
import java.math.BigDecimal;

public class ProductVariantDto {
    @JoinColumn(table = "product_id")
    private Product product;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String color;
    private BigDecimal price;
    private String size;
    private Integer stock;

    public ProductVariantDto() {
    }

    public ProductVariantDto(Product product, Long id, String color, BigDecimal price, String size, Integer stock) {
        this.product = product;
        this.id = id;
        this.color = color;
        this.price = price;
        this.size = size;
        this.stock = stock;
    }

    public ProductVariantDto(Product product, Long id, String size, String color, Integer stock, BigDecimal price) {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
}
