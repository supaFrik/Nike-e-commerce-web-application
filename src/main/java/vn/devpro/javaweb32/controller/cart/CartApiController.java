package vn.devpro.javaweb32.controller.cart;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.devpro.javaweb32.entity.cart.CartItem;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.repository.CredentialRepository;
import vn.devpro.javaweb32.service.CartService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartApiController {

    private final CartService cartService;
    private final CredentialRepository credentialRepository;

    public CartApiController(CartService cartService, CredentialRepository credentialRepository) {
        this.cartService = cartService;
        this.credentialRepository = credentialRepository;
    }

    private Customer getCurrentCustomer() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return credentialRepository.findByEmail(email)
                .map(c -> c.getCustomer())
                .orElse(null);
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<Map<String, Object>> addProductToCart(@PathVariable("id") Long productId,
                                                                @RequestParam(defaultValue = "1") int quantity,
                                                                @RequestParam(required = false) String size) {
        try {
            Customer customer = getCurrentCustomer();
            if (customer == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }

            cartService.addProduct(customer, productId, quantity, size);
            List<CartItem> cartItems = cartService.getCartItems(customer);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "itemCount", cartItems.stream().mapToInt(CartItem::getQuantity).sum(),
                    "subtotal", cartItems.stream().mapToDouble(CartItem::getTotal).sum()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> cartCount() {
        Customer customer = getCurrentCustomer();
        if(customer == null) {
            return ResponseEntity.ok(Map.of("error", "Unauthorized: User not found or not logged in."));
        }
        int itemCount = cartService.getCartItems(customer).stream().mapToInt(CartItem::getQuantity).sum();
        return ResponseEntity.ok(Map.of("itemCount", itemCount));
    }
}
