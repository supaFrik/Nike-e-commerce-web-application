package vn.demo.nike.features.coupon.mapper;

import org.springframework.stereotype.Component;
import vn.demo.nike.features.coupon.dto.request.CouponCreateRequest;
import vn.demo.nike.features.coupon.dto.response.CouponResponse;
import vn.demo.nike.features.coupon.entity.Coupon;

@Component
public class CouponMapper {

    public Coupon toEntity(CouponCreateRequest request) {
        if (request == null) return null;

        Coupon coupon = new Coupon();
        coupon.setCode(request.code());
        coupon.setDescription(request.description());
        coupon.setDiscountType(request.discountType());
        coupon.setDiscountValue(request.discountValue());
        coupon.setMinimumOrderAmount(request.minimumOrderAmount());
        coupon.setMaximumDiscount(request.maximumDiscount());
        coupon.setStartDate(request.startDate());
        coupon.setEndDate(request.endDate());
        coupon.setActive(request.active() != null ? request.active() : true);
        return coupon;
    }

    public CouponResponse toResponse(Coupon coupon) {
        if (coupon == null) return null;

        return new CouponResponse(
                coupon.getId(),
                coupon.getCode(),
                coupon.getDescription(),
                coupon.getDiscountType(),
                coupon.getDiscountValue(),
                coupon.getMinimumOrderAmount(),
                coupon.getMaximumDiscount(),
                coupon.getStartDate(),
                coupon.getEndDate(),
                coupon.getActive()
        );
    }
}