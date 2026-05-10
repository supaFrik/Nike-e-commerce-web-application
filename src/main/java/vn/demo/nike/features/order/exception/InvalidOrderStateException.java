package vn.demo.nike.features.order.exception;

import vn.demo.nike.features.order.enums.OrderStatus;

public class InvalidOrderStateException extends RuntimeException {

    public InvalidOrderStateException(Long orderId,
                                      OrderStatus currentStatus,
                                      OrderStatus expectedStatus) {
        super(String.format(
                "Order %s has status %s but expected %s",
                orderId,
                currentStatus,
                expectedStatus
        ));
    }
}
