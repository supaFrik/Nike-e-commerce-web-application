package vn.demo.nike.features.catalog.cart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartCountResponse {
    private final int itemCount;
}
