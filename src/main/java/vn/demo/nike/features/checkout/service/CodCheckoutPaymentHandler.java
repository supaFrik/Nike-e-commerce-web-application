package vn.demo.nike.features.checkout.service;

import org.springframework.stereotype.Component;
import vn.demo.nike.features.order.domain.Order;
import vn.demo.nike.features.checkout.dto.CheckoutItemSnapshotDto;
import vn.demo.nike.features.checkout.dto.CheckoutInitiationResponse;
import vn.demo.nike.features.payment.domain.enums.PaymentMethod;

import java.util.List;

@Component
public class CodCheckoutPaymentHandler implements CheckoutPaymentHandler {

    @Override
    public boolean supports(PaymentMethod paymentMethod) {
        return paymentMethod == PaymentMethod.COD;
    }

    @Override
    public CheckoutInitiationResponse handle(Order order, List<CheckoutItemSnapshotDto> snapshots) {
        int itemCount = snapshots.stream()
                .map(CheckoutItemSnapshotDto::getQuantity)
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
