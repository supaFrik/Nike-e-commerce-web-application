package vn.devpro.javaweb32.entity.order.enums;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public enum ShippingMethod {
    STANDARD(BigDecimal.valueOf(5000), "Standard Shipping", "Delivers in 3-5 business days", 3, 5),
    EXPRESS(BigDecimal.valueOf(15000), "Express Shipping", "Delivers in 1-2 business days", 1, 2),
    PICKUP(BigDecimal.ZERO, "In-Store Pickup", "Pickup today at selected store", 0, 1);

    private final BigDecimal cost;
    private final String displayName;
    private final String description;
    private final int deliveryDaysMin;
    private final int deliveryDaysMax;

    ShippingMethod(BigDecimal cost, String displayName, String description, int deliveryDaysMin, int deliveryDaysMax) {
        this.cost = cost;
        this.displayName = displayName;
        this.description = description;
        this.deliveryDaysMin = deliveryDaysMin;
        this.deliveryDaysMax = deliveryDaysMax;
    }

    public BigDecimal getCost() { return cost; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public int getDeliveryDaysMin() { return deliveryDaysMin; }
    public int getDeliveryDaysMax() { return deliveryDaysMax; }

    public String label() {
        String eta;
        if (deliveryDaysMin == deliveryDaysMax) {
            eta = deliveryDaysMin + (deliveryDaysMin == 1 ? " day" : " days");
        } else {
            eta = deliveryDaysMin + "-" + deliveryDaysMax + " days";
        }
        return displayName + " (" + eta + ")";
    }

    public static ShippingMethod safeValueOf(String raw) {
        if (raw == null) return STANDARD;
        try { return ShippingMethod.valueOf(raw.toUpperCase()); } catch (Exception ex) { return STANDARD; }
    }

    /** List all available methods - convenience for controllers / UI rendering. */
    public static List<ShippingMethod> available() { return Arrays.asList(values()); }
}
