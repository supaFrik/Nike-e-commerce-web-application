package vn.devpro.javaweb32.service.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.devpro.javaweb32.entity.cart.CartItem;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.entity.order.Order;
import vn.devpro.javaweb32.entity.order.OrderItem;
import vn.devpro.javaweb32.entity.order.enums.ShippingMethod;
import vn.devpro.javaweb32.repository.CartItemRepository;
import vn.devpro.javaweb32.repository.OrderItemRepository;
import vn.devpro.javaweb32.repository.OrderRepository;
import vn.devpro.javaweb32.service.order.dto.OrderSummary;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public double resolveShipping(ShippingMethod method) {
        return method.getCost().doubleValue();
    }

    public OrderSummary buildOrderSummary(Customer customer, ShippingMethod shippingMethod) {
        List<CartItem> cartItems = cartItemRepository.findByCustomer(customer);
        if (cartItems.isEmpty()) throw new IllegalStateException("Cart is empty");
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem ci : cartItems) {
            if (ci == null || ci.getProduct() == null) continue;
            var price = ci.getProduct().getEffectivePrice();
            if (price != null) {
                subtotal = subtotal.add(price.multiply(BigDecimal.valueOf(ci.getQuantity())));
            }
        }
        BigDecimal shipping = shippingMethod.getCost();
        BigDecimal discount = BigDecimal.ZERO; // placeholder for future discount engine
        BigDecimal total = subtotal.add(shipping).subtract(discount);
        return new OrderSummary(cartItems, subtotal, shipping, discount, total, shippingMethod);
    }

    @Transactional
    public Order createOrderFromCart(Customer customer, ShippingMethod shippingMethod) {
        List<CartItem> cartItems = cartItemRepository.findByCustomer(customer);
        if (cartItems.isEmpty()) throw new IllegalStateException("Cart is empty");
        Order order = new Order();
        order.setCustomer(customer);
        order.setShippingMethod(shippingMethod);
        // items
        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setProduct(ci.getProduct());
            oi.setProductName(ci.getProduct().getName());
            oi.setUnitPrice(ci.getProduct().getEffectivePrice());
            oi.setQuantity(ci.getQuantity());
            oi.setSize(ci.getSize());
            oi.setColor(ci.getColor());
            order.addItem(oi);
        }
        order.finalizeTotals();
        Order saved = orderRepository.save(order);
        cartItemRepository.deleteByCustomer(customer);
        return saved;
    }

    public Order findById(Long id) { return orderRepository.findById(id).orElse(null); }
}
