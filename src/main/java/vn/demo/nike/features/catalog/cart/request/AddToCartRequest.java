package vn.demo.nike.features.catalog.cart.request;

public class AddToCartRequest {

    private Long variantId;
    private Integer quantity;

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
