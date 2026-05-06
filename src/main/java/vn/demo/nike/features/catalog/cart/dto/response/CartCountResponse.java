package vn.demo.nike.features.catalog.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartCountResponse {
    private final int itemCount;
}
