package vn.demo.nike.features.coupon.dto.response;

import java.math.BigDecimal;

public record CouponCalculationResponse(
        String code,
        BigDecimal discountAmount,
        BigDecimal finalAmount,
        String message
) {
}
