package vn.devpro.javaweb32.controller.cart;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.devpro.javaweb32.entity.cart.CartItem;
import vn.devpro.javaweb32.entity.customer.Credential;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.repository.CredentialRepository;
import vn.devpro.javaweb32.service.customer.CartService;

import javax.servlet.http.HttpSession;

@Controller
public class CartController {

    private final CartService cartService;
    private final CredentialRepository credentialRepository;

    public CartController(CartService cartService, CredentialRepository credentialRepository) {
        this.cartService = cartService;
        this.credentialRepository = credentialRepository;
    }

    private Customer getCurrentCustomer() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return credentialRepository.findByEmail(email)
                .map(Credential::getCustomer)
                .orElse(null);
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession httpSession, Model model) {
        Customer customer = getCurrentCustomer();
        if(customer == null) return "redirect:/auth";
        var cartItems = cartService.getCartItems(customer);

        double subtotal = cartItems.stream().mapToDouble(CartItem::getTotal).sum();
        double shipping = 5.0;
        double tax = subtotal * 0.08;
        double total = subtotal + shipping + tax;

        model.addAttribute("discount", 0.0);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shipping", shipping);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);

        return "customer/cart";
    }
}
