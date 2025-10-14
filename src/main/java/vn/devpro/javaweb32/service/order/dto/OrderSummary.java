package vn.devpro.javaweb32.service.order.dto;

import vn.devpro.javaweb32.entity.cart.CartItem;
import vn.devpro.javaweb32.entity.order.enums.ShippingMethod;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class OrderSummary {
    private final List<CartItem> items;
    private final BigDecimal subtotal;
    private final BigDecimal shippingCost;
    private final BigDecimal discount;
    private final BigDecimal total;
    private final ShippingMethod shippingMethod;

    public OrderSummary(List<CartItem> items, BigDecimal subtotal, BigDecimal shippingCost, BigDecimal discount, BigDecimal total, ShippingMethod shippingMethod) {
        this.items = items == null ? List.of() : items;
        this.subtotal = subtotal == null ? BigDecimal.ZERO : subtotal;
        this.shippingCost = shippingCost == null ? BigDecimal.ZERO : shippingCost;
        this.discount = discount == null ? BigDecimal.ZERO : discount;
        this.total = total == null ? BigDecimal.ZERO : total;
        this.shippingMethod = shippingMethod;
    }

    public List<CartItem> getItems() { return Collections.unmodifiableList(items); }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getShippingCost() { return shippingCost; }
    public BigDecimal getDiscount() { return discount; }
    public BigDecimal getTotal() { return total; }
    public ShippingMethod getShippingMethod() { return shippingMethod; }
}

