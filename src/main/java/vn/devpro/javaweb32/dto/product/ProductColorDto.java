package vn.devpro.javaweb32.dto.product;

import vn.devpro.javaweb32.entity.product.Product;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

public class ProductColorDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String hex;

    @JoinColumn(name= "product_id")
    private Product product;

    public ProductColorDto() {
    }

    public ProductColorDto(Long id, String name, String hex, Product product) {
        this.id = id;
        this.name = name;
        this.hex = hex;
        this.product = product;
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

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
