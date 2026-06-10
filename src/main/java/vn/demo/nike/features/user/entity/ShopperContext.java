package vn.demo.nike.features.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShopperContext {
    // TODO 1: Decide the invariant for this context:
    //         - authenticated shopper must have userId and must not have guestId.
    //         - guest shopper must have guestId and must not have userId.
    // TODO 2: Add factory methods when you implement this exercise:
    //         - authenticated(Long userId)
    //         - guest(String guestId)
    // TODO 3: Add helper methods when needed by CartService:
    //         - isGuest()
    //         - requireUserId()
    //         - requireGuestId()
    // TODO 4: Validate constructor/factory inputs so invalid mixed states cannot be created.
    private final boolean authenticated;
    private final Long userId;
    private final String guestId;
}
