package vn.demo.nike.features.checkout.service;

import vn.demo.nike.features.order.entity.Order;
import vn.demo.nike.features.checkout.dto.response.CheckoutInitiationResponse;
import vn.demo.nike.features.checkout.model.CheckoutItemSnapshot;
import vn.demo.nike.infras.payment.vnpay.enums.PaymentMethod;

import java.util.List;

public interface CheckoutPaymentHandler {
    boolean supports(PaymentMethod paymentMethod);

    CheckoutInitiationResponse handle(Order order, List<CheckoutItemSnapshot> snapshots);
}
