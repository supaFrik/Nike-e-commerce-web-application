package vn.devpro.javaweb32.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public void addProduct(Customer customer, Long productId, int quantity, String size, String color) {
        Product product = productRepository.findById(productId)
                .orElseThrow();
        boolean validVariant = product.getVariants().stream()
            .anyMatch(v -> v.getSize().equals(size) && v.getColor().equals(color) && v.getStock() != null && v.getStock() > 0);
        if (!validVariant) {
            throw new IllegalArgumentException("Selected size and color combination is not available or out of stock.");
        }
        CartItem cartItem = cartItemRepository.findByCustomerAndProduct_idAndSizeAndColor(customer, productId, size, color);
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setSize(size);
            cartItem.setColor(color);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeProduct(Customer customer, Long productId, String size, String color) {
        cartItemRepository.deleteByCustomerAndProduct_idAndSizeAndColor(customer, productId, size, color);
    }

    @Transactional
    public void updateQuantity(Customer customer, Long productId, String size, String color, int quantity) {
        CartItem cartItem = cartItemRepository.findByCustomerAndProduct_idAndSizeAndColor(customer, productId, size, color);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }
}
