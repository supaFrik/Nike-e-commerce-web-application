package vn.demo.nike.features.checkout.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.demo.nike.features.catalog.cart.response.CartSummaryResponse;
import vn.demo.nike.features.order.domain.Order;
import vn.demo.nike.features.order.domain.enums.ShippingMethod;

@Getter
@AllArgsConstructor
public class CheckoutPageViewData {
    private final CartSummaryResponse cart;
    private final Order order;
    private final ShippingMethod selectedShippingMethod;
    private final boolean hasItems;
    private final boolean orderAccessible;
}
