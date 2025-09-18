package vn.devpro.javaweb32.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.devpro.javaweb32.dto.customer.auth.SignupForm;
import vn.devpro.javaweb32.service.AuthService;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/auth")
    public String authPage(Model model, @RequestParam(required = false) String error, @RequestParam(required = false) String logout,
                           @ModelAttribute("signupForm") SignupForm signupForm) {
        if (error != null) model.addAttribute("loginError", "Email or Password is invalid");
        if (logout != null) model.addAttribute("logoutMsg", "Logout Successfully!");
        return "customer/auth"; // tên JSP
    }

    // xử lý signup
    @PostMapping("/signup")
    public String doSignup(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           RedirectAttributes ra) {
        try {
            authService.register(username, email, password);
            ra.addFlashAttribute("signupSuccess", "Signup Successfully!");
            return "redirect:/auth";
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("signupError", ex.getMessage());
            ra.addFlashAttribute("signupForm", new SignupForm(username, email)); // để điền lại input
            return "redirect:/auth";
        }
    }
}
