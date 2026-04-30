package vn.demo.nike.features.order.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum ShippingMethod {
    STANDARD(BigDecimal.valueOf(5000), "Standard Shipping", "Delivers in 3-5 business days", 3, 5),
    EXPRESS(BigDecimal.valueOf(15000), "Express Shipping", "Delivers in 1-2 business days", 1, 2),
    PICKUP(BigDecimal.ZERO, "In-Store Pickup", "Pickup today at selected store", 0, 1);

    private final BigDecimal cost;
    private final String displayName;
    private final String description;
    private final int deliveryDaysMin;
    private final int deliveryDaysMax;

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

    public static List<ShippingMethod> available() { return Arrays.asList(values()); }
}
