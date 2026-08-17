package vn.demo.nike.features.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShopperContext {
    private final boolean authenticated;
    private final Long userId;
    private final String guestId;

    public static ShopperContext authenticated(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Authenticated shopper requires a user id");
        }
        return new ShopperContext(true, userId, null);
    }

    public static ShopperContext guest(String guestId) {
        if (guestId == null || guestId.isBlank()) {
            throw new IllegalArgumentException("Guest shopper requires a guest id");
        }
        return new ShopperContext(false, null, guestId);
    }

    public boolean isGuest() {
        return !authenticated;
    }
}
