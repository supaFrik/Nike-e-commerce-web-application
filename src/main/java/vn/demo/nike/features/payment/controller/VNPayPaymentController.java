package vn.demo.nike.features.payment.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.demo.nike.features.identity.user.request.CurrentUserProvider;
import vn.demo.nike.features.payment.enums.PaymentStatus;
import vn.demo.nike.features.payment.dto.VNPayCreatePaymentResponse;
import vn.demo.nike.features.payment.dto.VNPayIpnResponse;
import vn.demo.nike.features.payment.dto.VNPayReturnResponse;
import vn.demo.nike.features.payment.service.VNPayPaymentService;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments/vnpay")
public class VNPayPaymentController {

    private final VNPayPaymentService vnPayPaymentService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping("/orders/{orderId}")
    public ResponseEntity<VNPayCreatePaymentResponse> createPaymentUrl(@PathVariable Long orderId,
                                                                       HttpServletRequest request) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        return ResponseEntity.ok(vnPayPaymentService.createPaymentUrl(orderId, request, currentUserId));
    }

    @GetMapping("/return")
    public ResponseEntity<Void> handleReturn(@RequestParam Map<String, String> params) {
        VNPayReturnResponse response = vnPayPaymentService.handleReturn(params);
        Long orderId = response.getOrderId();
        boolean paymentSuccess = orderId != null
                && response.isSignatureValid()
                && response.getSuggestedPaymentStatus() == PaymentStatus.SUCCESS;
        String redirectUrl = paymentSuccess
                ? "/orders/" + orderId
                : orderId != null
                ? "/checkout?orderId=" + orderId
                : "/cart";

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }

    @GetMapping("/ipn")
    public ResponseEntity<VNPayIpnResponse> handleIpn(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(vnPayPaymentService.handleIpn(params));
    }
}
