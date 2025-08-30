package vn.devpro.javaweb32.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.devpro.javaweb32.entity.customer.Credential;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.repository.CredentialRepository;

import java.security.Principal;

@Controller
public class ProfileController {
    @Autowired
    private CredentialRepository credentialRepository;

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        String email = principal.getName();
        Credential credential = credentialRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        Customer customer = credential.getCustomer();
        model.addAttribute("customer", customer);
        return "customer/profile";
    }
}
