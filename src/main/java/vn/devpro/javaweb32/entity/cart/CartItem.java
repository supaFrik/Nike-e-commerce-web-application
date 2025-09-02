package vn.devpro.javaweb32.entity.cart;

import vn.devpro.javaweb32.entity.product.Product;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem() {
    }

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getTotalPrice() {
        return product.getPrice().multiply(java.math.BigDecimal.valueOf(quantity)).doubleValue();
    }
}
