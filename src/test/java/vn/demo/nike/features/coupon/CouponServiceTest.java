package vn.demo.nike.features.coupon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.demo.nike.features.coupon.dto.request.ApplyCouponRequest;
import vn.demo.nike.features.coupon.dto.response.CouponCalculationResponse;
import vn.demo.nike.features.coupon.entity.Coupon;
import vn.demo.nike.features.coupon.enums.DiscountType;
import vn.demo.nike.features.coupon.exception.CouponExpiredException;
import vn.demo.nike.features.coupon.exception.CouponNotFoundException;
import vn.demo.nike.features.coupon.exception.InvalidCouponException;
import vn.demo.nike.features.coupon.exception.OutOfLimitException;
import vn.demo.nike.features.coupon.mapper.CouponMapper;
import vn.demo.nike.features.coupon.repository.CouponRepository;
import vn.demo.nike.features.coupon.service.CouponService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {
    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponMapper couponMapper;

    @InjectMocks
    private CouponService couponService;

    private Coupon validCoupon() {
        Coupon c = new Coupon();
        c.setCode("NIKE2026");
        c.setDescription("Giảm 20% cho đơn hàng từ 500k");
        c.setDiscountType(DiscountType.PERCENTAGE);
        c.setDiscountValue(BigDecimal.valueOf(20));
        c.setMinimumOrderAmount(BigDecimal.valueOf(500000));
        c.setMaximumDiscount(BigDecimal.valueOf(200000));
        c.setStartDate(LocalDateTime.of(2026, 9, 18, 12, 28, 10));
        c.setEndDate(LocalDateTime.of(2026, 10, 18, 12, 28, 10));
        c.setUsageLimit(10);
        c.setUsageCount(0);
        c.setActive(true);
        return c;
    }

    @Test
    void shouldThrowWhenCouponNotFound() {
        ApplyCouponRequest request = new ApplyCouponRequest(
                "FAKE",
                BigDecimal.valueOf(100000)
        );
        when(couponRepository.findByCodeIgnoreCase(request.code())).thenReturn(Optional.empty());
        assertThrows(
                CouponNotFoundException.class,
                () -> couponService.applyCoupon(request)
        );
        verify(couponRepository).findByCodeIgnoreCase("FAKE");
    }

    @Test
    void shouldThrowWhenCouponInactive() {
        ApplyCouponRequest request = new ApplyCouponRequest(
                "NIKE2026",
                BigDecimal.valueOf(100000)
        );
        Coupon coupon = validCoupon();
        coupon.setActive(false);
        when(couponRepository.findByCodeIgnoreCase(request.code())).thenReturn(Optional.of(coupon));
        assertThrows(
                CouponExpiredException.class,
                () -> couponService.applyCoupon(request)
        );
        verify(couponRepository).findByCodeIgnoreCase("NIKE2026");
    }

    @Test
    void shouldThrowWhenCouponExpired() {
        ApplyCouponRequest request = new ApplyCouponRequest(
                "NIKE2026",
                BigDecimal.valueOf(100000)
        );
        Coupon coupon = validCoupon();
        coupon.setStartDate(LocalDateTime.of(2026, 8, 10, 12, 28, 10));
        coupon.setEndDate(LocalDateTime.of(2026, 9, 10, 12, 28, 10));
        when(couponRepository.findByCodeIgnoreCase(request.code())).thenReturn(Optional.of(coupon));
        assertThrows(
                CouponExpiredException.class,
                () -> couponService.applyCoupon(request)
        );
        verify(couponRepository).findByCodeIgnoreCase("NIKE2026");
    }

    @Test
    void shouldThrowWhenOrderBelowMinimum() {
        ApplyCouponRequest request = new ApplyCouponRequest(
                "NIKE2026",
                BigDecimal.valueOf(100000)
        );
        Coupon coupon = validCoupon();
        when(couponRepository.findByCodeIgnoreCase(request.code())).thenReturn(Optional.of(coupon));
        assertThrows(
                InvalidCouponException.class,
                () -> couponService.applyCoupon(request)
        );
        verify(couponRepository).findByCodeIgnoreCase("NIKE2026");
    }

    @Test
    void shouldThrowWhenUsageLimitExceeded() {
        ApplyCouponRequest request = new ApplyCouponRequest(
                "NIKE2026",
                BigDecimal.valueOf(100000)
        );
        Coupon coupon = validCoupon();
        coupon.setUsageCount(3);
        coupon.setUsageLimit(1);
        when(couponRepository.findByCodeIgnoreCase(request.code())).thenReturn(Optional.of(coupon));
        assertThrows(
                OutOfLimitException.class,
                () -> couponService.applyCoupon(request)
        );
        verify(couponRepository).findByCodeIgnoreCase("NIKE2026");
    }

    @Test
    void shouldApplyCouponPercentageCoupon() {
        ApplyCouponRequest request = new ApplyCouponRequest(
                "NIKE2026",
                BigDecimal.valueOf(1000000)
        );
        Coupon coupon = validCoupon();
        coupon.setMaximumDiscount(null);
        when(couponRepository.findByCodeIgnoreCase(request.code())).thenReturn(Optional.of(coupon));
        CouponCalculationResponse response = couponService.applyCoupon(request);
        assertEquals(
                new BigDecimal("200000.00"),
                response.discountAmount()
        );
    }

    @Test
    void shouldCapPercentageAtMaxDiscount() {
        ApplyCouponRequest request = new ApplyCouponRequest(
                "NIKE2026",
                BigDecimal.valueOf(2000000)
        );
        Coupon coupon = validCoupon();
        coupon.setMaximumDiscount(BigDecimal.valueOf(200000));
        when(couponRepository.findByCodeIgnoreCase(request.code()))
                .thenReturn(Optional.of(coupon));
        CouponCalculationResponse response = couponService.applyCoupon(request);
        assertEquals(
                BigDecimal.valueOf(200_000),
                response.discountAmount()
        );
    }
}
