package vn.demo.nike.features.checkout.service;

import org.springframework.stereotype.Component;
import vn.demo.nike.features.order.entity.Order;
import vn.demo.nike.features.checkout.model.CheckoutItemSnapshot;
import vn.demo.nike.features.checkout.dto.response.CheckoutInitiationResponse;
import vn.demo.nike.infras.payment.vnpay.enums.PaymentMethod;

import java.util.List;

@Component
public class CodCheckoutPaymentHandler implements CheckoutPaymentHandler {

    @Override
    public boolean supports(PaymentMethod paymentMethod) {
        return paymentMethod == PaymentMethod.COD;
    }

    @Override
    public CheckoutInitiationResponse handle(Order order, List<CheckoutItemSnapshot> snapshots) {
        int itemCount = snapshots.stream()
                .map(CheckoutItemSnapshot::getQuantity)
                .filter(quantity -> quantity != null)
                .mapToInt(Integer::intValue)
                .sum();

        return new CheckoutInitiationResponse(
                order.getId(),
                itemCount,
                order.getSubtotal(),
                order.getShippingCost(),
                order.getDiscount(),
                order.getTotal(),
                order.getOrderStatus(),
                order.getPaymentMethod(),
                false,
                null,
                null,
                snapshots
        );
    }
}
