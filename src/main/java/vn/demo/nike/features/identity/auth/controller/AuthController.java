package vn.demo.nike.features.identity.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.demo.nike.features.identity.auth.request.SignUpVerificationRequest;
import vn.demo.nike.features.identity.auth.request.SignupForm;
import vn.demo.nike.features.identity.auth.service.SignUpVerificationService;
import vn.demo.nike.features.identity.user.entity.User;
import vn.demo.nike.features.identity.user.enums.Role;
import vn.demo.nike.features.identity.user.repository.UserRepository;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuthController {

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

    @PostMapping("/api/auth/signup-verification-code")
    @ResponseBody
    public ResponseEntity<Map<String, String>> sendVerificationCode(@Valid @RequestBody SignUpVerificationRequest request) {
        String email = request.email().trim().toLowerCase();
        if (!userRepository.existsByEmail(email)) {
            signUpVerificationService.sendCode(email);
        }
        return ResponseEntity.ok(Map.of("message", "Code xác nhận đã được gửi. Vui lòng kiểm tra hộp thư email !"));
    }

    @GetMapping("/api/auth/check-duplicate")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkDuplicate(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {
        boolean usernameExists = username != null
                && !username.trim().isEmpty()
                && userRepository.existsByUsername(username.trim());
        boolean emailExists = email != null
                && !email.trim().isEmpty()
                && userRepository.existsByEmail(email.trim().toLowerCase());

        return ResponseEntity.ok(Map.of(
                "usernameExists", usernameExists,
                "emailExists", emailExists
        ));
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
