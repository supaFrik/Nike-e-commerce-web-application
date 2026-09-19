package vn.demo.nike.features.coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.demo.nike.features.coupon.dto.request.ApplyCouponRequest;
import vn.demo.nike.features.coupon.dto.request.CouponCreateRequest;
import vn.demo.nike.features.coupon.dto.response.CouponCalculationResponse;
import vn.demo.nike.features.coupon.dto.response.CouponResponse;
import vn.demo.nike.features.coupon.entity.Coupon;
import vn.demo.nike.features.coupon.enums.DiscountType;
import vn.demo.nike.features.coupon.exception.CouponExpiredException;
import vn.demo.nike.features.coupon.exception.CouponNotFoundException;
import vn.demo.nike.features.coupon.exception.InvalidCouponException;
import vn.demo.nike.features.coupon.exception.OutOfLimitException;
import vn.demo.nike.features.coupon.mapper.CouponMapper;
import vn.demo.nike.features.coupon.repository.CouponRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    public CouponResponse createCoupon(CouponCreateRequest request) {
        if(couponRepository.existsByCode(request.code())) {
            throw new InvalidCouponException("Coupon code already exists!");
        }
        Coupon coupon = couponMapper.toEntity(request);
        Coupon savedCoupon = couponRepository.save(coupon);

        return couponMapper.toResponse(savedCoupon);
    }

    @Transactional(readOnly = true)
    public CouponCalculationResponse applyCoupon(ApplyCouponRequest request) {
        Coupon coupon = couponRepository.findByCodeIgnoreCase(request.code()).orElseThrow(
                () -> new CouponNotFoundException(request.code())
        );

        LocalDateTime now = LocalDateTime.now();

        if (!coupon.getActive()) {
            throw new CouponExpiredException(coupon.getCode());
        }

        if (now.isBefore(coupon.getStartDate())
                || now.isAfter(coupon.getEndDate())) {
            throw new CouponExpiredException(coupon.getCode());
        }

        if (coupon.getUsageLimit() != null && coupon.getUsageCount() >= coupon.getUsageLimit()) {
            throw new OutOfLimitException(coupon.getCode());
        }

        if (request.orderAmount().compareTo(coupon.getMinimumOrderAmount()) < 0) {
            throw new InvalidCouponException("Coupon is invalid!");
        }

        BigDecimal discountAmount = calculateDiscountAmount(coupon, request.orderAmount());
        BigDecimal finalAmount = request.orderAmount().subtract(discountAmount);

        return new CouponCalculationResponse(coupon.getCode(), discountAmount, finalAmount, "Apply coupon successfully");
    }

    private BigDecimal calculateDiscountAmount(Coupon coupon, BigDecimal orderAmount) {
        BigDecimal discountValue = coupon.getDiscountValue();

        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            BigDecimal discountAmount = orderAmount
                    .multiply(discountValue)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            if (coupon.getMaximumDiscount() != null && discountAmount.compareTo(coupon.getMaximumDiscount()) > 0) {
                return coupon.getMaximumDiscount();
            }
            return discountAmount;
        }

        if (coupon.getDiscountType() == DiscountType.FIXED_AMOUNT) {
            return coupon.getDiscountValue().min(orderAmount);
        }

        return BigDecimal.ZERO;
    }

    @Transactional
    public void markCouponAsUsed(String code) {
        Coupon coupon = couponRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new CouponNotFoundException(code));
        coupon.setUsageCount(coupon.getUsageCount() + 1);
        couponRepository.save(coupon);
    }
}
