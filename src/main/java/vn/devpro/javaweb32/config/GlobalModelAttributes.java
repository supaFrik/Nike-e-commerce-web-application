package vn.devpro.javaweb32.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.web.csrf.CsrfToken;
import vn.devpro.javaweb32.entity.customer.Credential;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.repository.CredentialRepository;
import vn.devpro.javaweb32.service.CurrentUserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

@ControllerAdvice
public class GlobalModelAttributes {

    private final CredentialRepository credentialRepository;

    @Autowired
    public GlobalModelAttributes(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @ModelAttribute
    public void addGlobalAttributes(HttpServletRequest request, Principal principal, Model model) {
        Object csrfObj = request.getSession().getAttribute(CsrfToken.class.getName());
        if (csrfObj instanceof CsrfToken token) {
            model.addAttribute("_csrf", token);
        }

        if(principal != null) {
            String email = principal.getName();
            Optional<Credential> credential = credentialRepository.findByEmail(email);
            if(credential.isPresent()) {
                Credential c = credential.get();
                Customer customer = c.getCustomer();
                model.addAttribute("currentCustomer", customer);
            }
        }
    }
}
