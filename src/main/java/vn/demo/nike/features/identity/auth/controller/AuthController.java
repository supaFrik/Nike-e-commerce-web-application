package vn.demo.nike.features.identity.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.demo.nike.features.identity.auth.request.SignupForm;
import vn.demo.nike.features.identity.user.domain.User;
import vn.demo.nike.features.identity.user.domain.enums.Role;
import vn.demo.nike.features.identity.user.repository.UserRepository;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage(Authentication authentication, Model model) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }
        model.addAttribute("signupForm", new SignupForm());
        return "user/auth";
    }

    @PostMapping("/signup")
    public String signupPage(@Valid SignupForm signupForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 1. Validate form
        if(bindingResult.hasErrors()) {
            return "user/auth";
        }
        if(userRepository.existsByEmail(signupForm.getEmail())) {
            bindingResult.rejectValue("email", "email.exists" ,"Email already exists");
            return "user/auth";
        }

        User user = new User();

        user.setEmail(signupForm.getEmail());
        user.setUsername(signupForm.getUsername());
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        user.setRole(Role.USER);
        user.setLocked(false);
        user.setEnabled(true);

        userRepository.save(user);

        // 4. Message
        redirectAttributes.addFlashAttribute("signupSuccess", "Signup Successful!. Please login");

        return "redirect:/login";
    }
}
