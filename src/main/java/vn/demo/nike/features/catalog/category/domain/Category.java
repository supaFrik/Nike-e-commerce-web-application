package vn.demo.nike.features.catalog.category.domain;

import jakarta.persistence.*;
import lombok.*;
import vn.demo.nike.shared.domain.BaseEntity;
import vn.demo.nike.features.catalog.product.domain.Product;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;
}
