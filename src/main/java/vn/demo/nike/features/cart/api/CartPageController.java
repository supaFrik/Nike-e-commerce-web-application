package vn.demo.nike.features.cart.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.demo.nike.features.cart.service.CartService;

@Controller
@RequiredArgsConstructor
public class CartPageController {
    private final CartService cartService;

    @GetMapping("/cart")
    public String cartPage(Model model) {
        model.addAttribute("cart", cartService.getCurrentCart());
        return "user/cart";
    }
}
