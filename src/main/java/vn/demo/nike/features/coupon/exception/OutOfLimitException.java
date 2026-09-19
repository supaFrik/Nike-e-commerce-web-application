package vn.demo.nike.features.coupon.exception;

public class OutOfLimitException extends RuntimeException {
    public OutOfLimitException(String code) {
        super("Out of limit: " + code);
    }
}
