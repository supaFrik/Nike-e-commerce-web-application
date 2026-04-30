package vn.demo.nike.features.order.exception;

public class OrderIdAndUserIdNotFoundException extends RuntimeException {
    public OrderIdAndUserIdNotFoundException() {
        super("Order Id and User Id not found");
    }
}
