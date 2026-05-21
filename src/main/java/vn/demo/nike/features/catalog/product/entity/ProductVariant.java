package vn.demo.nike.features.catalog.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.demo.nike.shared.entity.BaseEntity;
import vn.demo.nike.features.catalog.product.enums.InventoryStatus;

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
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "inventory_status", length = 20)
    private InventoryStatus inventoryStatus = InventoryStatus.IN_ORDER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable = false)
    private ProductColor color;


}
