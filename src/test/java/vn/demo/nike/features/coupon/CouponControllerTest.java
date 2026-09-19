package vn.demo.nike.features.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vn.demo.nike.features.coupon.controller.CouponController;
import vn.demo.nike.features.coupon.dto.request.ApplyCouponRequest;
import vn.demo.nike.features.coupon.dto.request.CouponCreateRequest;
import vn.demo.nike.features.coupon.dto.response.CouponCalculationResponse;
import vn.demo.nike.features.coupon.dto.response.CouponResponse;
import vn.demo.nike.features.coupon.enums.DiscountType;
import vn.demo.nike.features.coupon.exception.CouponExpiredException;
import vn.demo.nike.features.coupon.exception.CouponNotFoundException;
import vn.demo.nike.features.coupon.exception.InvalidCouponException;
import vn.demo.nike.features.coupon.service.CouponService;
import vn.demo.nike.shared.exception.GlobalExceptionHandler;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CouponControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CouponService couponService;

    @InjectMocks
    private CouponController couponController;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(couponController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void applyCoupon_shouldReturn200() throws Exception {
        ApplyCouponRequest request = new ApplyCouponRequest("NIKE2026", BigDecimal.valueOf(1000000));
        CouponCalculationResponse response = new CouponCalculationResponse(
                "NIKE2026", BigDecimal.valueOf(200000), BigDecimal.valueOf(800000), "Apply coupon successfully"
        );

        when(couponService.applyCoupon(any())).thenReturn(response);

        mockMvc.perform(post("/api/coupons/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("NIKE2026"))
                .andExpect(jsonPath("$.discountAmount").value(200000));
    }

    @Test
    void createCoupon_shouldReturn200() throws Exception {
        CouponCreateRequest request = new CouponCreateRequest(
                null, "NIKE2026", "Desc", DiscountType.PERCENTAGE,
                BigDecimal.valueOf(20), BigDecimal.valueOf(500000), BigDecimal.valueOf(200000),
                LocalDateTime.of(2026, 9, 18, 0, 0), LocalDateTime.of(2026, 10, 18, 0, 0), true
        );
        CouponResponse response = new CouponResponse(
                1L, "NIKE2026", "Desc", DiscountType.PERCENTAGE,
                BigDecimal.valueOf(20), BigDecimal.valueOf(500000), BigDecimal.valueOf(200000),
                LocalDateTime.of(2026, 9, 18, 0, 0), LocalDateTime.of(2026, 10, 18, 0, 0), true
        );

        when(couponService.createCoupon(any())).thenReturn(response);

        mockMvc.perform(post("/api/coupons/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("NIKE2026"));
    }

    @Test
    void applyCoupon_whenNotFound_shouldReturn404() throws Exception {
        ApplyCouponRequest request = new ApplyCouponRequest("NOTFOUND", BigDecimal.valueOf(1000000));
        when(couponService.applyCoupon(any())).thenThrow(new CouponNotFoundException("NOTFOUND"));

        mockMvc.perform(post("/api/coupons/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void applyCoupon_whenExpired_shouldReturn400() throws Exception {
        ApplyCouponRequest request = new ApplyCouponRequest("EXPIRED", BigDecimal.valueOf(1000000));
        when(couponService.applyCoupon(any())).thenThrow(new CouponExpiredException("EXPIRED"));

        mockMvc.perform(post("/api/coupons/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void applyCoupon_whenInvalid_shouldReturn400() throws Exception {
        ApplyCouponRequest request = new ApplyCouponRequest("INVALID", BigDecimal.valueOf(1000000));
        when(couponService.applyCoupon(any())).thenThrow(new InvalidCouponException("Invalid"));

        mockMvc.perform(post("/api/coupons/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
