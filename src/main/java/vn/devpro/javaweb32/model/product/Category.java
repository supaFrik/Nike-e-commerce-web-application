package vn.devpro.javaweb32.model.product;

import vn.devpro.javaweb32.model.base.BaseModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category extends BaseModel {
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;

    // Default constructor
    public Category() {}

    // Constructor
    public Category(String name) {
        this.name = name;
    }

    // Constructor with products
    public Category(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    //Getters And Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
