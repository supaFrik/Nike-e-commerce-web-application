package vn.devpro.javaweb32.order;

import org.junit.jupiter.api.Test;
import vn.devpro.javaweb32.entity.order.Order;
import vn.devpro.javaweb32.entity.order.OrderItem;
import vn.devpro.javaweb32.entity.order.enums.ShippingMethod;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class OrderCalculationTest {

    @Test
    void finalizeTotalsComputesSubtotalAndTotal() {
        Order order = new Order();
        order.setShippingMethod(ShippingMethod.EXPRESS);
        order.setShippingCost(ShippingMethod.EXPRESS.getCost());

        OrderItem i1 = new OrderItem();
        i1.setUnitPrice(BigDecimal.valueOf(80000));
        i1.setQuantity(2);
        order.addItem(i1);

        OrderItem i2 = new OrderItem();
        i2.setUnitPrice(BigDecimal.valueOf(50000));
        i2.setQuantity(1);
        order.addItem(i2);

        order.finalizeTotals();

        assertEquals(0, BigDecimal.valueOf(210000).compareTo(order.getSubtotal()), "Subtotal mismatch");
        BigDecimal expectedTotal = BigDecimal.valueOf(210000).add(ShippingMethod.EXPRESS.getCost());
        assertEquals(0, expectedTotal.compareTo(order.getTotal()), "Total mismatch");
        assertEquals(0, BigDecimal.valueOf(160000).compareTo(i1.getLineTotal()));
        assertEquals(0, BigDecimal.valueOf(50000).compareTo(i2.getLineTotal()));
    }

    @Test
    void shippingMethodCosts() {
        assertEquals(BigDecimal.valueOf(5000), ShippingMethod.STANDARD.getCost());
        assertEquals(BigDecimal.valueOf(15000), ShippingMethod.EXPRESS.getCost());
        assertEquals(BigDecimal.ZERO, ShippingMethod.PICKUP.getCost());
    }

    @Test
    void safeValueOfGracefulFallback() {
        assertEquals(ShippingMethod.STANDARD, ShippingMethod.safeValueOf(null));
        assertEquals(ShippingMethod.STANDARD, ShippingMethod.safeValueOf("unknown"));
        assertEquals(ShippingMethod.EXPRESS, ShippingMethod.safeValueOf("express"));
    }

    @Test
    void setShippingMethodAutoSetsCostAndRawSelection() {
        Order order = new Order();
        // default should be STANDARD
        assertEquals(ShippingMethod.STANDARD, order.getShippingMethod());
        assertEquals(ShippingMethod.STANDARD.getCost(), order.getShippingCost());

        // Change to PICKUP => cost zero
        order.setShippingMethod(ShippingMethod.PICKUP);
        assertEquals(ShippingMethod.PICKUP.getCost(), order.getShippingCost());

        // Raw input (case-insensitive) to EXPRESS
        order.setShippingMethodRaw("express");
        assertEquals(ShippingMethod.EXPRESS, order.getShippingMethod());
        assertEquals(ShippingMethod.EXPRESS.getCost(), order.getShippingCost());

        // Unknown raw should fallback to STANDARD
        order.setShippingMethodRaw("some-weird-value");
        assertEquals(ShippingMethod.STANDARD, order.getShippingMethod());
        assertEquals(ShippingMethod.STANDARD.getCost(), order.getShippingCost());

        // Label contains expected pieces
        String label = ShippingMethod.EXPRESS.label();
        assertTrue(label.toLowerCase().contains("express"));
        assertTrue(label.contains("1-2"));
    }
}
