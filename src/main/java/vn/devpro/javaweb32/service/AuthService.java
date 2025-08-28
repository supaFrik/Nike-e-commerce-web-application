package vn.devpro.javaweb32.service;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.devpro.javaweb32.entity.customer.Credential;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.repository.CredentialRepository;
import vn.devpro.javaweb32.repository.CustomerRepository;

import javax.transaction.Transactional;

@Service
public class AuthService {
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    public AuthService(CredentialRepository credentialRepository, PasswordEncoder passwordEncoder, CustomerRepository customerRepository) {
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
    }


    @Transactional
    public Customer register(String username, String email, String rawPassword) {
        if(credentialRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }
        Customer customer = new Customer();
        customer.setUsername(username);

        Credential cred = new Credential();
        cred.setEmail(email);
        cred.setPasswordHash(passwordEncoder.encode(rawPassword));
        cred.setEnabled(true);
        cred.setCustomer(customer);

        customer.setCredential(cred);
        return customerRepository.save(customer);
    }
    public Customer login(String email, String rawPassword) {
        Credential credential = credentialRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(rawPassword, credential.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }
        return credential.getCustomer();
    }
}
