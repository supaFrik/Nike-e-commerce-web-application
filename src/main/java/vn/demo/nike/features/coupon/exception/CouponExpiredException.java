package vn.demo.nike.features.coupon.exception;

public class CouponExpiredException extends RuntimeException {
    public CouponExpiredException(String code) {
        super("Coupon is expired: " + code);
    }
}
