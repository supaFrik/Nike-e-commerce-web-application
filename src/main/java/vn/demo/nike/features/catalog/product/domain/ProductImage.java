package vn.demo.nike.features.catalog.product.domain;

import jakarta.persistence.*;
import lombok.*;
import vn.demo.nike.shared.domain.BaseEntity;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_images")
public class ProductImage extends BaseEntity {

    @Column(length = 1024, nullable = false)
    private String path;

    @Column(length = 255)
    private String title;

    @Column(length = 512)
    private String altText;

    @Column(name = "is_main")
    private Boolean isMainForColor = Boolean.FALSE;

    @Column(name = "order_index")
    private Integer orderIndex = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable = false)
    private ProductColor color;
}
