package vn.devpro.javaweb32.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.devpro.javaweb32.dto.customer.auth.SignupForm;
import vn.devpro.javaweb32.service.AuthService;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Hiển thị trang login / signup
     */
    @GetMapping("/auth")
    public String authPage(Model model,
                           @RequestParam(required = false) String error,
                           @RequestParam(required = false) String logout,
                           @ModelAttribute("signupForm") SignupForm signupForm) {

        if (error != null) {
            model.addAttribute("loginError", "Email or Password is invalid");
        }
        if (logout != null) {
            model.addAttribute("logoutMsg", "Logout Successfully!");
        }
        return "customer/auth";
    }

    /**
     * Đăng ký tài khoản
     */
    @PostMapping({"/signup", "/auth/signup"})
    public String doSignup(@Valid @ModelAttribute("signupForm") SignupForm signupForm,
                           BindingResult bindingResult,
                           RedirectAttributes ra) {

        // Kiểm tra trùng username/email trước khi gọi service
        if (!bindingResult.hasErrors()) {
            String username = trimOrNull(signupForm.getUsername());
            String email = normalizeEmail(signupForm.getEmail());

            if (username != null && authService.usernameExists(username)) {
                bindingResult.rejectValue("username", "username.exists", "Username is taken");
            }
            if (email != null && authService.emailExists(email)) {
                bindingResult.rejectValue("email", "email.exists", "Email already exists");
            }
        }

        // Nếu form có lỗi → quay lại trang auth
        if (bindingResult.hasErrors()) {
            addFlashBack(ra, signupForm, bindingResult);
            return "redirect:/auth";
        }

        // Thực hiện đăng ký
        try {
            authService.register(signupForm.getUsername(), signupForm.getEmail(), signupForm.getPassword());
            ra.addFlashAttribute("signupSuccess", "Signup Successfully!");
        } catch (IllegalArgumentException ex) {
            mapRegistrationException(ex.getMessage(), bindingResult);
            addFlashBack(ra, signupForm, bindingResult);
        }
        return "redirect:/auth";
    }

    /**
     * API dùng cho AJAX để kiểm tra trùng username/email
     */
    @GetMapping("/api/auth/check-duplicate")
    @ResponseBody
    public Map<String, Boolean> checkDuplicate(@RequestParam(required = false) String email,
                                               @RequestParam(required = false) String username) {
        return Map.of(
                "emailExists", email != null && authService.emailExists(normalizeEmail(email)),
                "usernameExists", username != null && authService.usernameExists(trimOrNull(username))
        );
    }

    // ---------- Helper methods ----------

    private void addFlashBack(RedirectAttributes ra, SignupForm form, BindingResult br) {
        ra.addFlashAttribute("org.springframework.validation.BindingResult.signupForm", br);
        ra.addFlashAttribute("signupForm", form);
    }

    private void mapRegistrationException(String msg, BindingResult br) {
        if (msg == null) return;
        String lower = msg.toLowerCase();

        if (lower.contains("email or username already exists")) {
            br.rejectValue("email", "email.exists", "Email already exists");
            br.rejectValue("username", "username.exists", "Username is taken");
        } else if (lower.contains("username is taken") || lower.contains("username already exists")) {
            br.rejectValue("username", "username.exists", "Username is taken");
        } else if (lower.contains("email already exists")) {
            br.rejectValue("email", "email.exists", "Email already exists");
        } else if (lower.contains("password must")) {
            br.rejectValue("password", "password.invalid", msg);
        } else if (lower.contains("username is required")) {
            br.rejectValue("username", "username.required", msg);
        } else if (lower.contains("email is required")) {
            br.rejectValue("email", "email.required", msg);
        } else if (lower.contains("password is required")) {
            br.rejectValue("password", "password.required", msg);
        } else {
            br.reject("signupError", msg);
        }
    }

    private String normalizeEmail(String email) {
        return (email == null || email.isBlank()) ? null : email.trim().toLowerCase();
    }

    private String trimOrNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
