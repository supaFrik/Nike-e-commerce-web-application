package vn.devpro.javaweb32.entity.order;

import vn.devpro.javaweb32.common.base.BaseEntity;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.entity.order.enums.OrderStatus;
import vn.devpro.javaweb32.entity.order.enums.ShippingMethod;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", length = 20, nullable = false)
    private OrderStatus orderStatus = OrderStatus.REVIEW;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipping_method", length = 15, nullable = false)
    private ShippingMethod shippingMethod = ShippingMethod.STANDARD;

    @Column(name = "subtotal", precision = 15, scale = 2, nullable = false)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "shipping_cost", precision = 15, scale = 2, nullable = false)
    private BigDecimal shippingCost = BigDecimal.ZERO;

    @Column(name = "discount", precision = 15, scale = 2, nullable = false)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "total", precision = 15, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = new Date();
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    public void finalizeTotals() {
        BigDecimal sum = BigDecimal.ZERO;
        for (OrderItem it : items) {
            if (it == null) continue;
            if (it.getUnitPrice() != null) {
                BigDecimal lt = it.getUnitPrice().multiply(BigDecimal.valueOf(it.getQuantity()));
                it.setLineTotal(lt);
                sum = sum.add(lt);
            }
        }
        this.subtotal = sum;
        if (shippingCost == null) shippingCost = BigDecimal.ZERO;
        if (discount == null) discount = BigDecimal.ZERO;
        this.total = subtotal.add(shippingCost).subtract(discount);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public OrderStatus getOrderStatus() { return orderStatus; }
    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }
    public ShippingMethod getShippingMethod() { return shippingMethod; }
    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
        if (shippingMethod != null) {
            this.shippingCost = shippingMethod.getCost();
        }
    }

    public void setShippingMethodRaw(String raw) {
        setShippingMethod(vn.devpro.javaweb32.entity.order.enums.ShippingMethod.safeValueOf(raw));
    }
    public void refreshShippingCostFromMethod() {
        if (this.shippingMethod != null) {
            this.shippingCost = this.shippingMethod.getCost();
        }
    }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getShippingCost() { return shippingCost; }
    public void setShippingCost(BigDecimal shippingCost) { this.shippingCost = shippingCost; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
