package vn.demo.nike.features.checkout.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.demo.nike.features.catalog.cart.dto.response.CartSummaryResponse;
import vn.demo.nike.features.order.entity.Order;
import vn.demo.nike.features.order.enums.ShippingMethod;

@Getter
@AllArgsConstructor
public class CheckoutPageResponse {
    private final CartSummaryResponse cart;
    private final Order order;
    private final ShippingMethod selectedShippingMethod;
    private final boolean hasItems;
    private final boolean orderAccessible;
}
