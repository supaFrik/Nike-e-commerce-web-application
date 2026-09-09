package vn.demo.nike.features.checkout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.demo.nike.features.catalog.cart.dto.response.CartSummaryResponse;
import vn.demo.nike.features.catalog.cart.service.CartService;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;
import vn.demo.nike.features.checkout.dto.response.CheckoutPageResponse;
import vn.demo.nike.features.checkout.service.CheckoutPageViewService;
import vn.demo.nike.features.order.entity.Order;
import vn.demo.nike.features.order.entity.OrderItem;
import vn.demo.nike.features.order.enums.ShippingMethod;
import vn.demo.nike.features.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckoutPageViewServiceTest {
    @Mock private CartService cartService;
    @Mock private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CheckoutPageViewService checkoutPageViewService;

    @Test
    void shouldReturnCartDataWhenCartHasItems() {
        CartSummaryResponse response = new CartSummaryResponse(
                List.of(), 2,
                BigDecimal.valueOf(200),
                BigDecimal.ZERO,
                BigDecimal.valueOf(200)
        );
        when(cartService.getCurrentCart()).thenReturn(response);

        CheckoutPageResponse result = checkoutPageViewService.buildCheckoutPage(1L, null);

        assertTrue(result.isHasItems());
        assertNull(result.getOrder());
        assertEquals(ShippingMethod.STANDARD, result.getSelectedShippingMethod());
        assertFalse(result.isOrderAccessible());

        verify(orderRepository, never()).findByIdAndUser_Id(anyLong(), anyLong());
    }

    @Test
    void shouldBuildSummaryFromOrderWhenCartEmpty() {
        CartSummaryResponse response = new CartSummaryResponse(
                List.of(), 0,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
        when(cartService.getCurrentCart()).thenReturn(response);

        Order order = buildOrderWithItems(
                BigDecimal.valueOf(300),
                BigDecimal.valueOf(20),
                BigDecimal.ZERO,
                BigDecimal.valueOf(310)
        );
        when(orderRepository.findByIdAndUser_Id(99L, 1L)).thenReturn(Optional.of(order));

        CheckoutPageResponse result = checkoutPageViewService.buildCheckoutPage(1L, 99L);

        assertNotNull(result.getCart());
        assertEquals(2, result.getCart().getItemCount());
        assertEquals(BigDecimal.valueOf(300), result.getCart().getSubtotal());
        assertTrue(result.isOrderAccessible());
        assertEquals(order, result.getOrder());
    }

    @Test
    void shouldFallbackToMostRecentOrderWhenCartEmptyAndNoOrderId() {
        CartSummaryResponse response = new CartSummaryResponse(
                List.of(), 0,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
        when(cartService.getCurrentCart()).thenReturn(response);

        Order recentOrder = buildOrderWithItems(
                BigDecimal.valueOf(300),
                BigDecimal.valueOf(20),
                BigDecimal.ZERO,
                BigDecimal.valueOf(310)
        );

        when(orderRepository.findTopByUser_IdOrderByCreateDateDesc(1L)).thenReturn(Optional.of(recentOrder));

        CheckoutPageResponse result = checkoutPageViewService.buildCheckoutPage(1L, null);

        assertNotNull(result.getCart());
        assertTrue(result.isOrderAccessible());
        assertEquals(recentOrder, result.getOrder());
        verify(orderRepository, never()).findByIdAndUser_Id(anyLong(), anyLong());
    }

    @Test
    void shouldReturnEmptyResponseWhenOrderNotFound() {
        CartSummaryResponse response = new CartSummaryResponse(
                List.of(), 0,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        when(cartService.getCurrentCart()).thenReturn(response);
        when(orderRepository.findTopByUser_IdOrderByCreateDateDesc(1L)).thenReturn(Optional.empty());

        CheckoutPageResponse result = checkoutPageViewService.buildCheckoutPage(1L, null);

        assertNotNull(result.getCart());
        assertEquals(0, result.getCart().getItemCount());
        assertFalse(result.isOrderAccessible());
        assertFalse(result.isHasItems());
        assertNull(result.getOrder());
    }

    private Order buildOrderWithItems(BigDecimal subtotal, BigDecimal shipping, BigDecimal discount, BigDecimal total) {
        Order order = new Order();
        order.setSubtotal(subtotal);
        order.setShippingCost(shipping);
        order.setDiscount(discount);
        order.setTotal(total);
        order.setShippingMethod(ShippingMethod.STANDARD);

        OrderItem item = new OrderItem();
        item.setProductId(1L);
        item.setProductName("Air Max 90");
        item.setUnitPrice(BigDecimal.valueOf(150));
        item.setQuantity(2);
        item.setLineTotal(BigDecimal.valueOf(300));
        item.setColor("White");
        item.setSize("42");

        order.setItems(List.of(item));
        return order;
    }


}
