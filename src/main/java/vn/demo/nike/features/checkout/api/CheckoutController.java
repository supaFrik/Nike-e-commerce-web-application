package vn.demo.nike.features.checkout.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.demo.nike.features.checkout.dto.PlaceCheckoutRequest;
import vn.demo.nike.features.checkout.dto.CheckoutInitiationResponse;
import vn.demo.nike.features.checkout.service.CheckoutService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<CheckoutInitiationResponse> placeOrder(@Valid @RequestBody PlaceCheckoutRequest request) {
        return ResponseEntity.ok(checkoutService.placeOrder(request));
    }
}
