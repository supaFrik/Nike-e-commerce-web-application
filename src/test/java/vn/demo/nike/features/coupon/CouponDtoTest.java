package vn.demo.nike.features.coupon;

import org.junit.jupiter.api.Test;
import vn.demo.nike.features.checkout.dto.request.PlaceCheckoutRequest;
import vn.demo.nike.features.coupon.dto.request.ApplyCouponRequest;
import vn.demo.nike.features.coupon.dto.request.CouponCreateRequest;
import vn.demo.nike.features.coupon.dto.response.CouponCalculationResponse;
import vn.demo.nike.features.coupon.dto.response.CouponResponse;
import vn.demo.nike.features.coupon.enums.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponDtoTest {

    @Test
    void placeCheckoutRequest_shouldGetAndSetFields() {
        PlaceCheckoutRequest req = new PlaceCheckoutRequest();
        req.setCouponCode("NIKE2026");
        req.setPaymentMethod("COD");
        req.setShippingMethod("STANDARD");
        req.setRecipientName("Nguyen Van A");
        req.setPhone("0901234567");
        req.setLine1("123 Street");
        req.setLine2("Apt 4B");
        req.setCity("Hanoi");
        req.setProvince("Hanoi");
        req.setPostalCode("100000");
        req.setCountry("VN");
        req.setNote("Giao gio hanh chinh");
        req.setAddressId(1L);

        assertEquals("NIKE2026", req.getCouponCode());
        assertEquals("COD", req.getPaymentMethod());
        assertEquals("STANDARD", req.getShippingMethod());
        assertEquals("Nguyen Van A", req.getRecipientName());
        assertEquals("0901234567", req.getPhone());
        assertEquals("123 Street", req.getLine1());
        assertEquals("Apt 4B", req.getLine2());
        assertEquals("Hanoi", req.getCity());
        assertEquals("Hanoi", req.getProvince());
        assertEquals("100000", req.getPostalCode());
        assertEquals("VN", req.getCountry());
        assertEquals("Giao gio hanh chinh", req.getNote());
        assertEquals(1L, req.getAddressId());

        PlaceCheckoutRequest allArgs = new PlaceCheckoutRequest(
                "NIKE2026", "COD", "STANDARD", "Note", 1L,
                "Name", "0901", "Line1", "Line2", "City", "Prov", "1000", "VN"
        );
        assertEquals("NIKE2026", allArgs.getCouponCode());
    }

    @Test
    void couponCreateRequest_shouldExposeRecordFields() {
        LocalDateTime now = LocalDateTime.now();
        CouponCreateRequest req = new CouponCreateRequest(
                1L, "CODE", "Desc", DiscountType.PERCENTAGE,
                BigDecimal.TEN, BigDecimal.valueOf(100), BigDecimal.valueOf(50),
                now, now.plusDays(7), true
        );

        assertEquals(1L, req.id());
        assertEquals("CODE", req.code());
        assertEquals("Desc", req.description());
        assertEquals(DiscountType.PERCENTAGE, req.discountType());
        assertEquals(BigDecimal.TEN, req.discountValue());
        assertEquals(BigDecimal.valueOf(100), req.minimumOrderAmount());
        assertEquals(BigDecimal.valueOf(50), req.maximumDiscount());
        assertEquals(now, req.startDate());
        assertEquals(now.plusDays(7), req.endDate());
        assertTrue(req.active());
    }

    @Test
    void couponResponse_shouldExposeRecordFields() {
        LocalDateTime now = LocalDateTime.now();
        CouponResponse res = new CouponResponse(
                1L, "CODE", "Desc", DiscountType.FIXED_AMOUNT,
                BigDecimal.valueOf(50000), BigDecimal.valueOf(200000), null,
                now, now.plusDays(5), true
        );

        assertEquals(1L, res.id());
        assertEquals("CODE", res.code());
        assertEquals(DiscountType.FIXED_AMOUNT, res.discountType());
        assertEquals(BigDecimal.valueOf(50000), res.discountValue());
        assertNull(res.maximumDiscount());
        assertTrue(res.active());
    }

    @Test
    void couponCalculationResponse_shouldExposeFields() {
        CouponCalculationResponse res = new CouponCalculationResponse(
                "CODE", BigDecimal.TEN, BigDecimal.valueOf(90), "Success"
        );
        assertEquals("CODE", res.code());
        assertEquals(BigDecimal.TEN, res.discountAmount());
        assertEquals(BigDecimal.valueOf(90), res.finalAmount());
        assertEquals("Success", res.message());
    }

    @Test
    void applyCouponRequest_shouldExposeFields() {
        ApplyCouponRequest req = new ApplyCouponRequest("CODE", BigDecimal.valueOf(500000));
        assertEquals("CODE", req.code());
        assertEquals(BigDecimal.valueOf(500000), req.orderAmount());
    }
}
