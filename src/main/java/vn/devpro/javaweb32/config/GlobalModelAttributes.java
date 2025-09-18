package vn.devpro.javaweb32.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.web.csrf.CsrfToken;
import vn.devpro.javaweb32.entity.cart.CartItem;
import vn.devpro.javaweb32.entity.customer.Credential;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.repository.CredentialRepository;
import vn.devpro.javaweb32.service.customer.CartService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

@ControllerAdvice
public class GlobalModelAttributes {

    private final CartService cartService;
    private final CredentialRepository credentialRepository;

    @Autowired
    public GlobalModelAttributes(CartService cartService, CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
        this.cartService = cartService;
    }

    @ModelAttribute
    public void addGlobalAttributes(HttpServletRequest request, Principal principal, Model model) {
        Object csrfObj = request.getSession().getAttribute(CsrfToken.class.getName());
        if (csrfObj instanceof CsrfToken token) {
            model.addAttribute("_csrf", token);
        }

        if(principal != null) {
            String email = principal.getName();
            Optional<Credential> credential = credentialRepository.findByEmail(email);
            if(credential.isPresent()) {
                Credential c = credential.get();
                Customer customer = c.getCustomer();
                model.addAttribute("currentCustomer", customer);
            }
        }
    }

    @ModelAttribute
    public void addGlobalAtrributes2(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            credentialRepository.findByEmail(authentication.getName()).ifPresent(c -> {
                model.addAttribute("currentCustomer", c.getCustomer());
                int count = cartService.getCartItems(c.getCustomer()).stream().mapToInt(CartItem::getQuantity).sum();
                model.addAttribute("cartCount", count);
            });
        } else {
            model.addAttribute("cartCount", 0);
        }
    }
}
