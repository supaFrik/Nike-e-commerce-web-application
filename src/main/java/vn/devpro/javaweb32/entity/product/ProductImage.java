package vn.devpro.javaweb32.entity.product;

import javax.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private Product variant;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
