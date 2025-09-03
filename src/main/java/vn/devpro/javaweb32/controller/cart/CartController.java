package vn.devpro.javaweb32.controller.cart;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.devpro.javaweb32.entity.cart.CartItem;
import vn.devpro.javaweb32.entity.customer.Credential;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.repository.CartItemRepository;
import vn.devpro.javaweb32.repository.CredentialRepository;
import vn.devpro.javaweb32.repository.CustomerRepository;
import vn.devpro.javaweb32.repository.ProductRepository;
import vn.devpro.javaweb32.service.CartService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final CustomerRepository customerRepository;
    private final CredentialRepository credentialRepository;

    public CartController(CartService cartService, CustomerRepository customerRepository, CredentialRepository credentialRepository) {
        this.cartService = cartService;
        this.customerRepository = customerRepository;
        this.credentialRepository = credentialRepository;
    }

    private Credential getCustomer() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return credentialRepository.findByEmail(email).orElseThrow();
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model,
                           @RequestParam(required = false) String coupon) {
        Customer customer = getCustomer().getCustomer();
        var cartItems = cartService.getCartItems(customer);

        double subTotal = cartItems.stream()
                .mapToDouble(CartItem::getTotal)
                .sum();
        double shipping = 5.0;
        double tax = subTotal * 0.08;

        double discount = 0.0;
        if(coupon != null && coupon.equalsIgnoreCase("SALE10")) {
            discount = subTotal * 0.1;
        } else if (subTotal > 100) {
            discount = 5.0;
        }

        double total = subTotal + shipping + tax - discount;


        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", subTotal);
        model.addAttribute("shipping", shipping);
        model.addAttribute("tax", tax);
        model.addAttribute("discount", discount);
        model.addAttribute("total", total);
        
        return "customer/cart";
    }

    @PostMapping("/add/{id}")
    public String addProduct(@PathVariable("id") Long id,
                             @RequestParam(defaultValue = "1") int quantity,
                             @RequestParam String size) {
        cartService.addProduct(getCustomer().getCustomer(), id, quantity, size);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeProduct(@PathVariable("id") Long id, @RequestParam String size) {
        cartService.removeProduct(getCustomer().getCustomer(), id, size);
        return "redirect:/cart";
    }
}
