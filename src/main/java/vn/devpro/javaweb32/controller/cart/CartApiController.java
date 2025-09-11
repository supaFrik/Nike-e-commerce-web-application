package vn.devpro.javaweb32.controller.cart;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.devpro.javaweb32.dto.cart.AddToCartRequest;
import vn.devpro.javaweb32.entity.cart.CartItem;
import vn.devpro.javaweb32.entity.customer.Credential;
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
        return credentialRepository.findByEmail(email).map(Credential::getCustomer).orElse(null);
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<?> addProductToCart(@PathVariable("id") Long productId,
                                              @RequestBody AddToCartRequest req) {
        try {
            Customer customer = getCurrentCustomer();
            if (customer == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }

            cartService.addProduct(customer, productId, req.getQuantity(), req.getSize(), req.getColor());
            List<CartItem> cartItems = cartService.getCartItems(customer);

            int itemCount = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
            double subtotal = cartItems.stream().mapToDouble(CartItem::getTotal).sum();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "itemCount", itemCount,
                    "subtotal", subtotal
            ));
        } catch (IllegalArgumentException ex) {
            // validation-like errors -> 400
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeProductFromCart(@PathVariable("id") Long productId,
                                                   @RequestParam String size,
                                                   @RequestParam String color) {
        try {
            Customer customer = getCurrentCustomer();
            if (customer == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }
            cartService.removeProduct(customer, productId, size, color);
            List<CartItem> cartItems = cartService.getCartItems(customer);
            int itemCount = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
            double subtotal = cartItems.stream().mapToDouble(CartItem::getTotal).sum();
            return ResponseEntity.ok(Map.of("success", true, "itemCount", itemCount, "subtotal", subtotal));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> cartCount() {
        Customer customer = getCurrentCustomer();
        if (customer == null) {
            return ResponseEntity.ok(Map.of("error", "Unauthorized: User not found or not logged in."));
        }
        int itemCount = cartService.getCartItems(customer).stream().mapToInt(CartItem::getQuantity).sum();
        return ResponseEntity.ok(Map.of("itemCount", itemCount));
    }
}
