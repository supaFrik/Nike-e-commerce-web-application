package vn.demo.nike.features.admin.page.dto;

import java.math.BigDecimal;

public record AdminOrderListItemResponse(
        Long id,
        String createdAtLabel,
        String customerName,
        String recipientName,
        String phone,
        String statusLabel,
        BigDecimal total,
        String shippingMethodLabel,
        int itemCount,
        String destinationLabel,
        String paymentMethodLabel
) {
}
