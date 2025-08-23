package vn.devpro.javaweb32.entity.product;

import vn.devpro.javaweb32.entity.base.BaseModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
