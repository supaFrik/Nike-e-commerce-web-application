package vn.devpro.javaweb32.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.devpro.javaweb32.entity.customer.Credential;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.repository.CredentialRepository;
import vn.devpro.javaweb32.repository.CustomerRepository;

import javax.transaction.Transactional;
import java.util.regex.Pattern;

@Service
public class AuthService {
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    // Strong password: min 8, one upper, one lower, one digit
    private static final Pattern STRONG_PASSWORD = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$");

    public AuthService(CredentialRepository credentialRepository, PasswordEncoder passwordEncoder, CustomerRepository customerRepository) {
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer register(String username, String email, String rawPassword, String confirmPassword) {
        if (isBlank(username)) {
            throw new IllegalArgumentException("Username is required");
        }
        if (isBlank(email)) {
            throw new IllegalArgumentException("Email is required");
        }
        if (isBlank(rawPassword)) {
            throw new IllegalArgumentException("Password is required");
        }
        if (!rawPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (!isStrongPassword(rawPassword)) {
            throw new IllegalArgumentException("Password must be at least 8 chars and include uppercase, lowercase, and a number");
        }

        String normalizedEmail = email.trim().toLowerCase();
        String normalizedUsername = username.trim();

        // Fast exists checks to avoid hitting DB twice later
        if (credentialRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (customerRepository.existsByUsername(normalizedUsername)) {
            throw new IllegalArgumentException("Username already exists");
        }

        Credential cred = new Credential();
        cred.setEmail(normalizedEmail);
        cred.setPasswordHash(passwordEncoder.encode(rawPassword));

        Customer customer = new Customer();
        customer.setUsername(normalizedUsername);
        customer.setCredential(cred);
        cred.setCustomer(customer);

        try {
            return customerRepository.save(customer);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Failed to register user. Please try again.");
        }
    }

    public Customer login(String email, String rawPassword) {
        if (isBlank(email) || isBlank(rawPassword)) {
            throw new IllegalArgumentException("Email and password are required");
        }
        String normalizedEmail = email.trim().toLowerCase();
        Credential credential = credentialRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Email or password is invalid"));

        if (!passwordEncoder.matches(rawPassword, credential.getPasswordHash())) {
            throw new IllegalArgumentException("Email or password is invalid");
        }
        return credential.getCustomer();
    }

    public boolean emailExists(String email) {
        if (email == null) return false;
        return credentialRepository.existsByEmail(email.trim().toLowerCase());
    }

    public boolean usernameExists(String username) {
        if (username == null) return false;
        return customerRepository.existsByUsername(username.trim());
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean isStrongPassword(String pwd) {
        return STRONG_PASSWORD.matcher(pwd).matches();
    }
}
