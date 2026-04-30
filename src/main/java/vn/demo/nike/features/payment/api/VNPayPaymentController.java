package vn.demo.nike.features.payment.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/orders/{orderId}")
    public ResponseEntity<VNPayCreatePaymentResponse> createPaymentUrl(@PathVariable Long orderId,
                                                                       HttpServletRequest request) {
        return ResponseEntity.ok(vnPayPaymentService.createPaymentUrl(orderId, request));
    }

    @GetMapping("/return")
    public ResponseEntity<Void> handleReturn(@RequestParam Map<String, String> params) {
        VNPayReturnResponse response = vnPayPaymentService.handleReturn(params);
        Long orderId = response.getOrderId();
        String redirectUrl = orderId != null
                ? "/orders/" + orderId
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
