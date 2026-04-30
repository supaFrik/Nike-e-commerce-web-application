package vn.demo.nike.features.cart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddToCartResponse {
    private final boolean success;
    private final String message;
    private final int itemCount;
}
