package vn.demo.nike.shared.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.demo.nike.features.identity.user.request.CurrentUserProvider;
import vn.demo.nike.features.identity.user.request.ContactForm;
import vn.demo.nike.features.cart.repository.CartItemRepository;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final CurrentUserProvider currentUserProvider;
    private final CartItemRepository cartItemRepository;

    @ModelAttribute("contactForm")
    public ContactForm contactForm() {
        return new ContactForm();
    }

    @ModelAttribute("cartCount")
    public int cartCount() {
        Long userId = currentUserProvider.getCurrentUserId();
        if (userId == null) {
            return 0;
        }
        return cartItemRepository.sumQuantityByUser_Id(userId);
    }
}
