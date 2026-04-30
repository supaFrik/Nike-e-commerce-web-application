package vn.demo.nike.features.checkout.service;

import vn.demo.nike.features.order.domain.Order;
import vn.demo.nike.features.checkout.dto.CheckoutInitiationResponse;
import vn.demo.nike.features.checkout.dto.CheckoutItemSnapshotDto;
import vn.demo.nike.features.payment.domain.enums.PaymentMethod;

import java.util.List;

public interface CheckoutPaymentHandler {
    boolean supports(PaymentMethod paymentMethod);

    CheckoutInitiationResponse handle(Order order, List<CheckoutItemSnapshotDto> snapshots);
}
