package vn.demo.nike.features.identity.user.request;

import vn.demo.nike.features.identity.user.entity.ShopperContext;

public interface CurrentShopperProvider {
    // TODO: Return the current shopper for cart/checkout ownership.
    //       Implementation must resolve authenticated users from Spring Security first.
    //       If no authenticated user exists, implementation must resolve or create a guest_id cookie.
    //       Services must not trust shopper identity from request body.
    ShopperContext getCurrentShopperContext();
}
