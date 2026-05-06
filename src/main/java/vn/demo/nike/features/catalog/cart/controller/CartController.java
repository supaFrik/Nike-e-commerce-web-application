package vn.demo.nike.features.catalog.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import vn.demo.nike.features.catalog.cart.dto.request.AddToCartRequest;
import vn.demo.nike.features.catalog.cart.dto.request.UpdateCartItemQuantityRequest;
import vn.demo.nike.features.catalog.cart.dto.response.AddToCartResponse;
import vn.demo.nike.features.catalog.cart.dto.response.CartCountResponse;
import vn.demo.nike.features.catalog.cart.dto.response.CartSummaryResponse;
import vn.demo.nike.features.catalog.cart.service.CartService;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name="Cart", description = "Cart operations")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Add item to cart")
    @ApiResponse(responseCode = "200", description = "Item added")
    @ApiResponse(responseCode = "400", description = "Invalid request"
            , content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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
