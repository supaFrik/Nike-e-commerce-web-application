package vn.demo.nike.features.coupon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.demo.nike.features.coupon.enums.DiscountType;
import vn.demo.nike.shared.entity.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupons")
public class Coupon extends BaseEntity {
    @Column(name = "code", unique = true, nullable = false, length = 50)
    @NotNull
    private String code;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @Column(name = "discount_value", precision = 15, scale = 2)
    @NotNull
    private BigDecimal discountValue;

    @Column(name = "minimum_order_amount", precision = 15, scale = 2)
    private BigDecimal minimumOrderAmount;

    @Column(name = "maximum_discount", precision = 15, scale = 2)
    private BigDecimal maximumDiscount;

    @Column(name = "start_date")
    @NotNull
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @NotNull
    private LocalDateTime endDate;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "usage_count")
    private Integer usageCount = 0;

    @Column(name = "active")
    private Boolean active = true;

    public Coupon(String code, String description, DiscountType discountType, BigDecimal bigDecimal, BigDecimal bigDecimal1, BigDecimal bigDecimal2, LocalDateTime localDateTime, LocalDateTime localDateTime1, Boolean active) {
    }
}
