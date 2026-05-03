package vn.demo.nike.features.order.domain;

import jakarta.persistence.*;
import lombok.*;
import vn.demo.nike.shared.domain.BaseEntity;

import java.math.BigDecimal;
@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "variant_id")
    private Long variantId;

    @Column(name = "sku", length = 100)
    private String sku;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "size", length = 20)
    private String size;

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "unit_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "line_total", precision = 15, scale = 2, nullable = false)
    private BigDecimal lineTotal;
}