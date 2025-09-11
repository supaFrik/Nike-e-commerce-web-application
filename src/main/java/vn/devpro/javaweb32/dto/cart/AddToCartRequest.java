package vn.devpro.javaweb32.dto.cart;

public class AddToCartRequest {
    private int quantity = 1;
    private String color;
    private String size;

    public AddToCartRequest() {
    }

    public AddToCartRequest(int quantity, String color, String size) {
        this.quantity = quantity;
        this.color = color;
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
