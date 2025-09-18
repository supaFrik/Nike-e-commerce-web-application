package vn.devpro.javaweb32.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.devpro.javaweb32.dto.customer.SimpleCustomerDto;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.repository.CredentialRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CurrentUserService {
    private final CredentialRepository credentialRepository;

    @Autowired
    public CurrentUserService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public Optional<SimpleCustomerDto> getCurrentCustomer() {
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
    return Optional.empty();
}
String email = auth.getName();
return credentialRepository.findByEmail(email)
        .map(credential1 -> {
            Customer customer = credential1.getCustomer();
            Long id = customer != null ? customer.getId() : null;
            String username = customer != null ? customer.getUsername() : email;
            return new SimpleCustomerDto(id, username);
        });
    }
}
