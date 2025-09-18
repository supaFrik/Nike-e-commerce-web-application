package vn.devpro.javaweb32.entity.product;

import vn.devpro.javaweb32.common.base.BaseEntity;
import vn.devpro.javaweb32.entity.base.BaseModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
public class Category extends BaseEntity {
    private  String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
