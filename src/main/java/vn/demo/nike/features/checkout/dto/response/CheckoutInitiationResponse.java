package vn.demo.nike.features.checkout.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vn.demo.nike.features.checkout.model.CheckoutItemSnapshot;
import vn.demo.nike.features.order.enums.OrderStatus;
import vn.demo.nike.features.payment.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutInitiationResponse {
    private Long orderId;
    private Integer itemCount;
    private BigDecimal subtotal;
    private BigDecimal shippingCost;
    private BigDecimal discount;
    private BigDecimal total;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;
    private boolean paymentRequired;
    private String paymentUrl;
    private String txnRef;
    private List<CheckoutItemSnapshot> items;
}
