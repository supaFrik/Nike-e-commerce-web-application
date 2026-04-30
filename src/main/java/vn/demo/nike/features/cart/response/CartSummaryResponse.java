package vn.demo.nike.features.cart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class CartSummaryResponse {
    private final List<CartItemViewResponse> items;
    private final int itemCount;
    private final BigDecimal subtotal;
    private final BigDecimal discount;
    private final BigDecimal total;
}
