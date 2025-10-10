package vn.devpro.javaweb32.entity.product;

import vn.devpro.javaweb32.common.base.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_favourite",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}),
        indexes = { @Index(name = "idx_user_product", columnList = "user_id,product_id") })
public class UserFavourite extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "favourited_at", nullable = false)
    private Date favouritedAt = new Date();

    public UserFavourite() {}

    public UserFavourite(Long userId, Product product) {
        this.userId = userId;
        this.product = product;
        this.favouritedAt = new Date();
    }

    public Long getId() { return super.getId(); }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Date getFavouritedAt() { return favouritedAt; }
    public void setFavouritedAt(Date favouritedAt) { this.favouritedAt = favouritedAt; }
}
