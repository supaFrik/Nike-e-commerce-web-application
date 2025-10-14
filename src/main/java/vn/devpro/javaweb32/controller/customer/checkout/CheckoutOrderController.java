package vn.devpro.javaweb32.controller.customer.checkout;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.devpro.javaweb32.entity.cart.CartItem;
import vn.devpro.javaweb32.entity.customer.Credential;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.repository.CredentialRepository;
import vn.devpro.javaweb32.service.customer.CartService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping
public class CheckoutOrderController {

    private static final String CHECKOUT_COMPLETED_FLAG = "CHECKOUT_COMPLETED";

    private final CartService cartService;
    private final CredentialRepository credentialRepository;

    public CheckoutOrderController(CartService cartService, CredentialRepository credentialRepository) {
        this.cartService = cartService;
        this.credentialRepository = credentialRepository;
    }

    private Customer getCurrentCustomer() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return credentialRepository.findByEmail(email).map(Credential::getCustomer).orElse(null);
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        Customer customer = getCurrentCustomer();
        if (customer == null) return "redirect:/auth";
        List<CartItem> items = cartService.getCartItems(customer);
        boolean hasItems = !items.isEmpty();
        if (!hasItems) {
            return "redirect:/cart?empty";
        }
        // Order not yet accessible until completion form posted
        model.addAttribute("orderAccessible", false);
        model.addAttribute("hasItems", true);
        return "customer/checkout";
    }

    @PostMapping("/checkout/complete")
    public String completeCheckout(HttpSession session) {
        // In real implementation you'd validate shipping/payment submission
        session.setAttribute(CHECKOUT_COMPLETED_FLAG, Boolean.TRUE);
        return "redirect:/order";
    }

    @GetMapping("/order")
    public String order(HttpSession session, Model model) {
        Customer customer = getCurrentCustomer();
        if (customer == null) return "redirect:/auth";
        List<CartItem> items = cartService.getCartItems(customer);
        if (items.isEmpty()) {
            return "redirect:/cart?empty";
        }
        boolean checkoutDone = Boolean.TRUE.equals(session.getAttribute(CHECKOUT_COMPLETED_FLAG));
        if (!checkoutDone) {
            return "redirect:/checkout";
        }
        model.addAttribute("orderAccessible", true);
        model.addAttribute("hasItems", true);
        return "customer/order"; // expect an order.jsp view (not yet provided)
    }
}

