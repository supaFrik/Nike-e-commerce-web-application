package vn.demo.nike.features.auth.controller;

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
import vn.demo.nike.features.auth.request.SignupForm;
import vn.demo.nike.features.auth.service.SignUpVerificationService;
import vn.demo.nike.features.user.entity.User;
import vn.demo.nike.features.user.enums.Role;
import vn.demo.nike.features.user.repository.UserRepository;

@Controller
@RequiredArgsConstructor
public class AuthPageController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignUpVerificationService signUpVerificationService;

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
        if (bindingResult.hasErrors()) {
            return "user/auth";
        }

        String normalizedUsername = signupForm.getUsername().trim();
        String normalizedEmail = signupForm.getEmail().trim().toLowerCase();
        signupForm.setUsername(normalizedUsername);
        signupForm.setEmail(normalizedEmail);

        if (userRepository.existsByUsername(normalizedUsername)) {
            bindingResult.rejectValue("username", "username.exists", "Tên đã tồn tại");
            return "user/auth";
        }
        if (userRepository.existsByEmail(normalizedEmail)) {
            bindingResult.rejectValue("email", "email.exists", "Email đã tồn tại");
            return "user/auth";
        }

        boolean verified = signUpVerificationService.verifyCode(
                normalizedEmail,
                signupForm.getVerificationCode()
        );

        if (!verified) {
            bindingResult.rejectValue("verificationCode", "verificationCode.invalid", "Mã không hợp lệ");
            return "user/auth";
        }

        User user = new User();
        user.setEmail(normalizedEmail);
        user.setUsername(normalizedUsername);
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        user.setRole(Role.USER);
        user.setLocked(false);
        user.setEnabled(true);

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("signupSuccess", "Đăng kí thành công. Vui lòng đăng nhập lại !");

        return "redirect:/login";
    }
}
