package vn.demo.nike.features.coupon.dto.response;

import vn.demo.nike.features.coupon.enums.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponResponse (
        Long id,
        String code,
        String description,
        DiscountType discountType,
        BigDecimal discountValue,
        BigDecimal minimumOrderAmount,
        BigDecimal maximumDiscount,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Boolean active
) {
}
