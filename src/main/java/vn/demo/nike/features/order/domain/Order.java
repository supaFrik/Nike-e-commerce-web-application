package vn.demo.nike.features.order.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.demo.nike.features.identity.user.domain.User;
import vn.demo.nike.features.order.domain.enums.OrderStatus;
import vn.demo.nike.features.order.domain.enums.ShippingMethod;
import vn.demo.nike.features.payment.domain.enums.PaymentMethod;
import vn.demo.nike.shared.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "order_status", length = 20, nullable = false)
    private OrderStatus orderStatus = OrderStatus.PENDING_PAYMENT;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "shipping_method", length = 15, nullable = false)
    private ShippingMethod shippingMethod = ShippingMethod.STANDARD;

    @Column(name = "shipping_recipient_name", length = 255, nullable = false)
    private String shippingRecipientName;

    @Column(name = "shipping_phone", length = 50, nullable = false)
    private String shippingPhone;

    @Column(name = "shipping_line1", length = 255, nullable = false)
    private String shippingLine1;

    @Column(name = "shipping_line2", length = 255)
    private String shippingLine2;

    @Column(name = "shipping_city", length = 100, nullable = false)
    private String shippingCity;

    @Column(name = "shipping_province", length = 100)
    private String shippingProvince;

    @Column(name = "shipping_postal_code", length = 30)
    private String shippingPostalCode;

    @Column(name = "shipping_country", length = 100, nullable = false)
    private String shippingCountry;

    @Column(name = "subtotal", precision = 15, scale = 2, nullable = false)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "shipping_cost", precision = 15, scale = 2, nullable = false)
    private BigDecimal shippingCost = BigDecimal.ZERO;

    @Column(name = "discount", precision = 15, scale = 2, nullable = false)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "total", precision = 15, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();
}
