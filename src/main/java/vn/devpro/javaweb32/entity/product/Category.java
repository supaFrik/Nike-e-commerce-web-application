package vn.devpro.javaweb32.entity.product;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import vn.devpro.javaweb32.common.base.BaseEntity;
import vn.devpro.javaweb32.entity.base.BaseModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
public class Category extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Product> products;

    // Constructors
    public Category() {
        super();
    }

    public Category(String name) {
        this.name = name;
    }

    // Getters and Setters
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
