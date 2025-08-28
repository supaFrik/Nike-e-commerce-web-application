package vn.devpro.javaweb32.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.devpro.javaweb32.service.AuthService;
import vn.devpro.javaweb32.service.CustomerService;

@Controller
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/signup")
    public String signupPage(
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String password,
            RedirectAttributes redirectAttributes
    ) {
        try {
            authService.register(username, email, rawPassword);
            redirectAttributes.addFlashAttribute("success", "Registration Successful");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }
}
