package vn.demo.nike.features.payment.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import vn.demo.nike.features.payment.domain.enums.PaymentStatus;
import vn.demo.nike.features.payment.dto.VNPayReturnResponse;
import vn.demo.nike.features.payment.service.VNPayPaymentService;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VNPayPaymentControllerTest {

    @Mock
    private VNPayPaymentService vnPayPaymentService;

    @InjectMocks
    private VNPayPaymentController vnPayPaymentController;

    @Test
    void handleReturn_redirectsFailedPaymentBackToCheckout() {
        when(vnPayPaymentService.handleReturn(Map.of("vnp_TxnRef", "ORD1")))
                .thenReturn(new VNPayReturnResponse(77L, "ORD1", true, "24", "02", PaymentStatus.FAILED));

        var response = vnPayPaymentController.handleReturn(Map.of("vnp_TxnRef", "ORD1"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getFirst(HttpHeaders.LOCATION))
                .isEqualTo("/checkout?orderId=77");
    }

    @Test
    void handleReturn_redirectsSuccessfulPaymentToOrderPage() {
        when(vnPayPaymentService.handleReturn(Map.of("vnp_TxnRef", "ORD2")))
                .thenReturn(new VNPayReturnResponse(88L, "ORD2", true, "00", "00", PaymentStatus.SUCCESS));

        var response = vnPayPaymentController.handleReturn(Map.of("vnp_TxnRef", "ORD2"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getFirst(HttpHeaders.LOCATION))
                .isEqualTo("/orders/88");
    }
}
