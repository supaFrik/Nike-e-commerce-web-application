package vn.demo.nike.features.coupon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ApplyCouponRequest(
        @NotBlank(message = "Coupon code is required")
        String code,
        @NotNull
        BigDecimal orderAmount
) {

}
