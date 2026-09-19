package vn.demo.nike.features.coupon.dto.request;

import vn.demo.nike.features.coupon.enums.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponCreateRequest(
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
