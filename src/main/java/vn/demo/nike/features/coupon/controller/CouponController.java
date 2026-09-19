package vn.demo.nike.features.coupon.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.demo.nike.features.coupon.dto.request.ApplyCouponRequest;
import vn.demo.nike.features.coupon.dto.request.CouponCreateRequest;
import vn.demo.nike.features.coupon.dto.response.CouponCalculationResponse;
import vn.demo.nike.features.coupon.dto.response.CouponResponse;
import vn.demo.nike.features.coupon.service.CouponService;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/apply")
    public ResponseEntity<CouponCalculationResponse> applyCoupon(
            @Valid @RequestBody ApplyCouponRequest request
    ) {
        return ResponseEntity.ok(couponService.applyCoupon(request));
    }

    @PostMapping("/admin/create")
    public ResponseEntity<CouponResponse> createCoupon(
            @Valid @RequestBody CouponCreateRequest request
    ) {
        return ResponseEntity.ok(couponService.createCoupon(request));
    }
}
