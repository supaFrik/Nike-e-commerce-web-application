package vn.demo.nike.features.coupon;

import org.junit.jupiter.api.Test;
import vn.demo.nike.features.coupon.dto.request.CouponCreateRequest;
import vn.demo.nike.features.coupon.entity.Coupon;
import vn.demo.nike.features.coupon.enums.DiscountType;
import vn.demo.nike.features.coupon.mapper.CouponMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponMapperTest {

    private final CouponMapper mapper = new CouponMapper();

    private CouponCreateRequest validRequest() {
        return new CouponCreateRequest(
                null,
                "NIKE2026",
                "Test coupon",
                DiscountType.PERCENTAGE,
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(500000),
                BigDecimal.valueOf(200000),
                LocalDateTime.of(2026, 9, 18, 0, 0),
                LocalDateTime.of(2026, 10, 18, 0, 0),
                true
        );
    }

    private Coupon validCoupon() {
        Coupon c = new Coupon();
        c.setId(1L);
        c.setCode("NIKE2026");
        c.setDescription("Test coupon");
        c.setDiscountType(DiscountType.PERCENTAGE);
        c.setDiscountValue(BigDecimal.valueOf(20));
        c.setMinimumOrderAmount(BigDecimal.valueOf(500000));
        c.setMaximumDiscount(BigDecimal.valueOf(200000));
        c.setStartDate(LocalDateTime.of(2026, 9, 18, 0, 0));
        c.setEndDate(LocalDateTime.of(2026, 10, 18, 0, 0));
        c.setActive(true);
        return c;
    }

    @Test
    void toEntity_shouldReturnNullWhenRequestIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toEntity_shouldMapAllFields() {
        Coupon result = mapper.toEntity(validRequest());

        assertEquals("NIKE2026", result.getCode());
        assertEquals("Test coupon", result.getDescription());
        assertEquals(DiscountType.PERCENTAGE, result.getDiscountType());
        assertEquals(BigDecimal.valueOf(20), result.getDiscountValue());
        assertEquals(BigDecimal.valueOf(500000), result.getMinimumOrderAmount());
        assertEquals(BigDecimal.valueOf(200000), result.getMaximumDiscount());
        assertTrue(result.getActive());
    }

    @Test
    void toEntity_shouldDefaultActiveTrueWhenNull() {
        CouponCreateRequest req = new CouponCreateRequest(
                null, "X", null, DiscountType.FIXED_AMOUNT,
                BigDecimal.ONE, null, null,
                LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                null   // active = null
        );
        assertTrue(mapper.toEntity(req).getActive());
    }

    @Test
    void toResponse_shouldReturnNullWhenCouponIsNull() {
        assertNull(mapper.toResponse(null));
    }

    @Test
    void toResponse_shouldMapAllFields() {
        var result = mapper.toResponse(validCoupon());

        assertEquals(1L, result.id());
        assertEquals("NIKE2026", result.code());
        assertEquals(DiscountType.PERCENTAGE, result.discountType());
        assertEquals(BigDecimal.valueOf(20), result.discountValue());
        assertTrue(result.active());
    }
}
