package vn.demo.nike.features.checkout.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.demo.nike.features.identity.user.domain.Address;
import vn.demo.nike.features.identity.user.domain.User;
import vn.demo.nike.features.identity.user.repository.UserRepository;
import vn.demo.nike.features.identity.user.request.CurrentUserProvider;
import vn.demo.nike.features.checkout.dto.CheckoutPageViewData;
import vn.demo.nike.features.order.domain.enums.ShippingMethod;
import vn.demo.nike.features.checkout.service.CheckoutPageViewService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CheckoutPageController {
    private final CurrentUserProvider currentUserProvider;
    private final UserRepository userRepository;
    private final CheckoutPageViewService checkoutPageViewService;

    @GetMapping("/checkout")
    public String checkoutPage(@RequestParam(required = false) Long orderId, Model model) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (currentUserId == null) {
            return "redirect:/login";
        }

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalStateException("Current user not found"));
        CheckoutPageViewData viewData = checkoutPageViewService.buildCheckoutPage(currentUserId, orderId);
        Address selectedAddress = currentUser.getAddress();
        List<Address> addresses = currentUser.getAddresses() == null ? List.of() : currentUser.getAddresses();

        model.addAttribute("cart", viewData.getCart());
        model.addAttribute("checkoutOrder", viewData.getOrder());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("addresses", addresses);
        model.addAttribute("selectedAddress", selectedAddress);
        model.addAttribute("shippingMethods", ShippingMethod.available());
        model.addAttribute("selectedShippingMethod", viewData.getSelectedShippingMethod());
        model.addAttribute("currentStep", 2);
        model.addAttribute("hasItems", viewData.isHasItems());
        model.addAttribute("orderAccessible", viewData.isOrderAccessible());

        return "user/checkout";
    }
}
