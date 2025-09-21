package vn.devpro.javaweb32.service.customer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.devpro.javaweb32.entity.cart.CartItem;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.repository.CartItemRepository;
import vn.devpro.javaweb32.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

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
       if(customer == null) throw new IllegalArgumentException("Customer is null");
       if(quantity < 0) throw new IllegalArgumentException("Quantity is negative");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        boolean variantAvailable = product.getVariants().stream()
                .filter(v -> v != null)
                .anyMatch(v -> v.getSize() != null && v.getSize().equals(size) &&
                        v.getColor() != null && v.getColor().getColorName().equals(color) &&
                        v.getStock() != null && v.getStock() >= quantity);

        if(!variantAvailable) {
            throw new IllegalArgumentException("Selected size/color is currently not available or out of stock");
        }

        Optional<CartItem> existing = cartItemRepository.findByCustomerAndProduct_idAndSizeAndColor(customer,  productId, size, color);

        CartItem cartItem;
        if(existing.isPresent()) {
            cartItem = existing.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        else {
            cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setSize(size);
            cartItem.setColor(color);
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
        }
        cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeProduct(Customer customer, Long productId, String size, String color) {
        cartItemRepository.deleteByCustomerAndProduct_idAndSizeAndColor(customer, productId, size, color);
    }

    @Transactional
    public void updateQuantity(Customer customer, Long productId, String size, String color, int quantity) {
        var opt = cartItemRepository.findByCustomerAndProduct_idAndSizeAndColor(customer, productId, size, color);
        if (opt.isPresent()) {
            CartItem cartItem = opt.get();
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }
}
