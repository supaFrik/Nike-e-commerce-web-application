package vn.devpro.javaweb32.controller.cart;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.devpro.javaweb32.entity.cart.CartItem;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.repository.ProductRepository;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final ProductRepository productRepository;

    public CartController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Lay gio hang tu SESSION
    private Map<Long, CartItem> getCart(HttpSession session) {
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("CART");
        if(cart == null) {
            cart = new HashMap<Long, CartItem>();
            session.setAttribute("CART", cart);
        }
        return cart;
    }

    // Hien thi gio hang
    @GetMapping
    public String getCart(HttpSession session, Model model) {
        Map<Long, CartItem> cart = getCart(session);
        double total = cart.values().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        model.addAttribute("cartItems", cart.values());
        model.addAttribute("total", total);
        return "cart/view";
    }

    // Them san pham vao gio
    @PostMapping("/add/{id}")
    public String addProduct(@PathVariable("id") Long id,
                             @RequestParam(defaultValue = "1") int quantity,
                             HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        Product product = productRepository.findById(id).orElse(null);
        if(product != null) {
            if(cart.containsKey(product.getId())) {
                cart.get(product.getId()).setQuantity(quantity);
            }
        }
        return "redirect:/cart";
    }

    //Xoa san pham
    @GetMapping("/remove/{id}")
    public String removeProduct(@PathVariable("id") Long id, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        cart.remove(id);
        return "redirect:/cart";
    }

    //Cap nhat so luong
    @PostMapping("/update/{id}")
    public String updateItem(@PathVariable("id") Long id,
                             @RequestParam int quantity,
                             HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        if(cart.containsKey(id)) {
            cart.get(id).setQuantity(quantity);
        }
        return "redirect:/cart";
    }
}
