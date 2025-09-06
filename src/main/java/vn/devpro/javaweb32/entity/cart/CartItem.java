package vn.devpro.javaweb32.entity.cart;

import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.entity.product.Product;

import javax.persistence.*;

@Entity
@Table(name = "cart_items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"customer_id", "product_id", "size"})
})
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Product product;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Customer customer;

    private int quantity;

    private String size;

    public CartItem() {
    }

    public CartItem(Product product, int quantity, Customer customer, String size) {
        this.product = product;
        this.quantity = quantity;
        this.customer = customer;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getTotal() {
        return product.getPrice().multiply(java.math.BigDecimal.valueOf(quantity)).doubleValue();
    }
}
