package vn.demo.nike.features.user.request;

import vn.demo.nike.features.user.entity.ShopperContext;

public interface CurrentShopperProvider {
    ShopperContext getCurrentShopperContext();
}
