package vn.demo.nike.features.catalog.product.domain;

import jakarta.persistence.*;
import lombok.*;
import vn.demo.nike.shared.domain.BaseEntity;
import vn.demo.nike.features.catalog.product.domain.enums.InventoryStatus;

@Getter
@Setter
@Builder
@Entity
@Table(name = "product_variants",
        uniqueConstraints = @UniqueConstraint(columnNames = {"color_id", "size"}))
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant extends BaseEntity {

    @Column(length = 100)
    private String sku;

    @Column(name = "size", length = 50)
    private String size;

    @Column(name = "stock")
    private Integer stock = 0;

    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    @Enumerated(EnumType.STRING)
    @Column(name = "inventory_status", length = 20)
    private InventoryStatus inventoryStatus = InventoryStatus.IN_ORDER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable = false)
    private ProductColor color;


}
