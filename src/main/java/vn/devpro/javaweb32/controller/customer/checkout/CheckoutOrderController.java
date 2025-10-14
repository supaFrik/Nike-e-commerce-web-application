package vn.devpro.javaweb32.controller.customer.checkout;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import vn.devpro.javaweb32.entity.cart.CartItem;
import vn.devpro.javaweb32.entity.customer.Credential;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.entity.order.Order;
import vn.devpro.javaweb32.entity.order.enums.ShippingMethod;
import vn.devpro.javaweb32.repository.CredentialRepository;
import vn.devpro.javaweb32.service.customer.CartService;
import vn.devpro.javaweb32.service.order.OrderService;
import vn.devpro.javaweb32.service.order.dto.OrderSummary;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping
public class CheckoutOrderController {

    private static final String CHECKOUT_COMPLETED_FLAG = "CHECKOUT_COMPLETED";

    private final CartService cartService;
    private final CredentialRepository credentialRepository;
    private final OrderService orderService;

    public CheckoutOrderController(CartService cartService, CredentialRepository credentialRepository, OrderService orderService) {
        this.cartService = cartService;
        this.credentialRepository = credentialRepository;
        this.orderService = orderService;
    }

    private Customer getCurrentCustomer() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return credentialRepository.findByEmail(email).map(Credential::getCustomer).orElse(null);
    }

    private double resolveShippingCost(ShippingMethod method) {
        return method == null ? ShippingMethod.STANDARD.getCost().doubleValue() : method.getCost().doubleValue();
    }

    private ShippingMethod parseShippingMethod(String raw) {
        return ShippingMethod.safeValueOf(raw);
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        Customer customer = getCurrentCustomer();
        if (customer == null) return "redirect:/auth";
        ShippingMethod shippingMethod = ShippingMethod.STANDARD; // default pre-selection
        OrderSummary summary;
        try {
            summary = orderService.buildOrderSummary(customer, shippingMethod);
        } catch (IllegalStateException ex) {
            return "redirect:/cart?empty";
        }
        model.addAttribute("orderAccessible", false);
        model.addAttribute("hasItems", true);
        model.addAttribute("items", summary.getItems());
        model.addAttribute("subtotal", summary.getSubtotal());
        model.addAttribute("shippingCost", summary.getShippingCost());
        model.addAttribute("discount", summary.getDiscount());
        model.addAttribute("total", summary.getTotal());
        model.addAttribute("currentStep", 2);
        model.addAttribute("shippingMethod", summary.getShippingMethod().name());
        model.addAttribute("shippingMethods", ShippingMethod.available());
        return "customer/checkout";
    }

    @PostMapping("/checkout/complete")
    public String completeCheckout(HttpSession session, @RequestParam(name = "shippingMethod", required = false) String shippingMethodRaw) {
        Customer customer = getCurrentCustomer();
        if (customer == null) return "redirect:/auth";
        ShippingMethod method = ShippingMethod.safeValueOf(shippingMethodRaw);
        Order order;
        try {
            order = orderService.createOrderFromCart(customer, method);
        } catch (IllegalStateException ex) {
            return "redirect:/cart?empty";
        }
        session.setAttribute("LAST_ORDER_ID", order.getId());
        session.setAttribute(CHECKOUT_COMPLETED_FLAG, Boolean.TRUE);
        return "redirect:/order/" + order.getId();
    }

    @GetMapping("/order")
    public String order(HttpSession session) {
        Long orderId = (Long) session.getAttribute("LAST_ORDER_ID");
        if (orderId != null) {
            return "redirect:/order/" + orderId;
        }
        return "redirect:/checkout";
    }

    @GetMapping("/order/{id}")
    public String orderById(@PathVariable("id") Long id, HttpSession session, Model model) {
        Customer customer = getCurrentCustomer();
        if (customer == null) return "redirect:/auth";
        if (id == null) return "redirect:/checkout";
        Order orderEntity = orderService.findById(id);
        if (orderEntity == null || !orderEntity.getCustomer().getId().equals(customer.getId())) {
            return "redirect:/checkout";
        }
        session.setAttribute("LAST_ORDER_ID", id);
        model.addAttribute("orderAccessible", true);
        model.addAttribute("hasItems", orderEntity.getItems() != null && !orderEntity.getItems().isEmpty());
        model.addAttribute("currentStep", 3);
        model.addAttribute("order", orderEntity);
        return "customer/order";
    }
}
