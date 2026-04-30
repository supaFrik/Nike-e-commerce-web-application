package vn.demo.nike.features.order.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.demo.nike.features.identity.user.request.CurrentUserProvider;
import vn.demo.nike.features.order.service.OrderPageViewService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderPageController {
    private final CurrentUserProvider currentUserProvider;
    private final OrderPageViewService orderPageViewService;

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (currentUserId == null) {
            return "redirect:/login";
        }

        model.addAttribute("orderPage", orderPageViewService.getOrderPage(id, currentUserId));
        model.addAttribute("orderDetailPath", "/orders/" + id);
        model.addAttribute("currentStep", 3);
        model.addAttribute("orderAccessible", true);
        model.addAttribute("hasItems", false);
        return "user/order";
    }
}
