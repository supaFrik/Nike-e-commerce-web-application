package vn.demo.nike.features.catalog.cart.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.demo.nike.features.catalog.cart.request.AddToCartRequest;
import vn.demo.nike.features.catalog.cart.request.UpdateCartItemQuantityRequest;
import vn.demo.nike.features.catalog.cart.response.AddToCartResponse;
import vn.demo.nike.features.catalog.cart.response.CartCountResponse;
import vn.demo.nike.features.catalog.cart.response.CartSummaryResponse;
import vn.demo.nike.features.catalog.cart.service.CartService;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Add to cart request
    @PostMapping("/items")
    public ResponseEntity<AddToCartResponse> addToCart(@RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(request));
    }

    // List cart item request
    @GetMapping
    public ResponseEntity<CartSummaryResponse> getCurrentCart() {
        return ResponseEntity.ok(cartService.getCurrentCart());
    }

    // Update cart item request
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<CartSummaryResponse> updateCartItemQuantity(@PathVariable Long cartItemId,
                                                                      @RequestBody UpdateCartItemQuantityRequest request) {
        return ResponseEntity.ok(cartService.updateCartItemQuantity(cartItemId, request));
    }

    // Remove cart item request
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartSummaryResponse> removeCartItem(@PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.removeCartItem(cartItemId));
    }

    // Count helper for cart logo
    @GetMapping("/count")
    public ResponseEntity<CartCountResponse> getCartCount() {
        return ResponseEntity.ok(cartService.getCartCount());
    }
}
