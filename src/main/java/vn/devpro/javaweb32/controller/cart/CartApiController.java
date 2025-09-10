package vn.devpro.javaweb32.controller.cart;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.devpro.javaweb32.dto.cart.CartItemDto;
import vn.devpro.javaweb32.entity.cart.CartItem;
import vn.devpro.javaweb32.entity.customer.Credential;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.repository.CredentialRepository;
import vn.devpro.javaweb32.service.CartService;

import java.security.Principal;
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
                .map(Credential::getCustomer)
                .orElse(null);
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<Map<String, Object>> addProductToCart(@PathVariable("id") Long productId,
                                                                @RequestParam(defaultValue = "1") int quantity,
                                                                @RequestParam String size,
                                                                @RequestParam String color,
                                                                Principal principal) {
        try {
            Customer customer = getCurrentCustomer();
            if (customer == null) {
                return ResponseEntity.status(401).body(Map.of("Error", "Unauthorized"));
            }
            cartService.addProduct(customer, productId, quantity, size, color);
            List<CartItem> cartItems = cartService.getCartItems(customer);

            List<CartItemDto> dtos = cartItems.stream().map(
                    item -> new CartItemDto(
                            item.getProduct().getId(),
                            item.getProduct().getName(),
                            item.getProduct().getPrice(),
                            item.getProduct().getDescription(),
                            item.getProduct().getImageUrl()
                    )
            ).toList();

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

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Map<String, Object>> removeProductFromCart(@PathVariable("id") Long productId,
                                                                     @RequestParam String size,
                                                                     @RequestParam String color) {
        try {
            Customer customer = getCurrentCustomer();
            if(customer == null) {
                return ResponseEntity.status(401).body(Map.of("Error", "Unauthorized"));
            }

            cartService.removeProduct(customer, productId, size, color);
            List<CartItem> cartItems = cartService.getCartItems(customer);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "itemCount", cartItems.stream().mapToInt(CartItem::getQuantity).sum(),
                    "subTotal", cartItems.stream().mapToDouble(CartItem::getTotal).sum()
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
