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
        boolean validVariant = product.getVariants().stream()
            .anyMatch(v -> v.getSize().equals(size) && v.getColor().equals(color) && v.getStock() != null && v.getStock() > 0);
        boolean variantAvailable = product.getVariants().stream()
                .filter(v -> v != null)
                .anyMatch(v -> v.getSize() == size &&
                        v.getColor().equals(color) &&
                        v.getStock() >= quantity &&
                        v.getStock() != null);
        if(!variantAvailable) {
            throw new IllegalArgumentException("Selected size/color is currently not available");
        }

        Optional<CartItem> existing = cartItemRepository.findByCustomerAndProduct_idAndSizeAndColor(customer,  productId, size, color);

        CartItem cartItem = new CartItem();
        if(existing.isPresent()) {
            cartItem = existing.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        else {
            CartItem newCartItem = new CartItem();
            newCartItem.setQuantity(quantity);
            newCartItem.setSize(size);
            newCartItem.setColor(color);
            newCartItem.setCustomer(customer);
            newCartItem.setProduct(product);
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
