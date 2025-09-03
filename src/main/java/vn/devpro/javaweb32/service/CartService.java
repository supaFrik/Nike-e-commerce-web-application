package vn.devpro.javaweb32.service;

import org.springframework.stereotype.Service;
import vn.devpro.javaweb32.entity.cart.CartItem;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.repository.CartItemRepository;
import vn.devpro.javaweb32.repository.ProductRepository;

import java.util.List;

@Service
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public List<CartItem> getCartItems(Customer customer) {
        return cartItemRepository.findByCustomer(customer);
    }

    // Them san pham
    public void addProduct(Customer customer, Long productId, int quantity, String size) {
        Product product = productRepository.findById(productId)
                .orElseThrow();
        CartItem cartItem = cartItemRepository.findByCustomerAndProduct_idAndSize(customer, productId, size);
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setSize(size);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItemRepository.save(cartItem);
    }

    public void removeProduct(Customer customer, Long productId, String size) {
        cartItemRepository.deleteByCustomerAndProduct_idAndSize(customer, productId, size);
    }

    public void updateQuantity(Customer customer, Long productId, String size, int quantity) {
        CartItem cartItem = cartItemRepository.findByCustomerAndProduct_idAndSize(customer, productId, size);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }
}
