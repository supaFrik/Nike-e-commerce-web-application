package vn.demo.nike.features.cart.domain;

import jakarta.persistence.*;
import lombok.*;
import vn.demo.nike.features.catalog.product.domain.ProductVariant;
import vn.demo.nike.features.identity.user.domain.User;
import vn.demo.nike.shared.domain.BaseEntity;

@Entity
@Table(name = "cart_items", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_user_variant",
                columnNames = {"user_id", "variant_id"}
        )
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    @Column(nullable = false)
    private Integer quantity;
}
