package vn.demo.nike.shared.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.demo.nike.features.user.request.CurrentUserProvider;
import vn.demo.nike.features.user.request.ContactForm;
import vn.demo.nike.features.catalog.cart.repository.CartItemRepository;
import vn.demo.nike.features.user.repository.UserRepository;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final CurrentUserProvider currentUserProvider;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

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

    @ModelAttribute("currentUserDisplayName")
    public String currentUserDisplayName() {
        Long userId = currentUserProvider.getCurrentUserId();
        if (userId == null) {
            return "";
        }

        return userRepository.findById(userId)
                .map(user -> {
                    String username = user.getUsername();
                    if (username != null && !username.isBlank()) {
                        return username;
                    }
                    return user.getEmail();
                })
                .orElse("");
    }
}
