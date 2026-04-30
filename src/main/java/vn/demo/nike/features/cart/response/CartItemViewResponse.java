package vn.demo.nike.features.cart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CartItemViewResponse {
    private final Long cartItemId;
    private final Long productId;
    private final String productName;
    private final String categoryName;
    private final String imageUrl;
    private final String colorName;
    private final String size;
    private final BigDecimal unitPrice;
    private final Integer quantity;
    private final BigDecimal lineTotal;
    private final Integer stock;
    private final Boolean active;
}
