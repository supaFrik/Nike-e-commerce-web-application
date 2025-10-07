package vn.devpro.javaweb32.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import vn.devpro.javaweb32.entity.customer.Credential;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.repository.CredentialRepository;
import vn.devpro.javaweb32.repository.CustomerRepository;
import vn.devpro.javaweb32.service.AuthService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SignupPersistenceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Register creates Customer + Credential with encoded password")
    @Transactional
    void registerPersistsEntities() {
        String rawPassword = "Abcdefg1";
        String username = "user_" + UUID.randomUUID().toString().substring(0,8);
        String email = "test_" + UUID.randomUUID().toString().substring(0,8) + "@example.com";

        Customer created = authService.register(username, email, rawPassword);
        assertNotNull(created.getCredential(), "Credential should be linked to customer");

        // Fetch by email to ensure persisted
        Credential cred = credentialRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new AssertionError("Credential not found after registration"));

        assertEquals(username, created.getUsername());
        assertEquals(email.toLowerCase(), cred.getEmail());
        assertTrue(passwordEncoder.matches(rawPassword, cred.getPasswordHash()), "Password should be encoded and match raw");
        assertNotEquals(rawPassword, cred.getPasswordHash(), "Password must not be stored in plain text");
        assertEquals("USER", cred.getRole(), "Default role should be USER");
    }

    @Test
    @DisplayName("Duplicate email is rejected")
    @Transactional
    void duplicateEmailRejected() {
        String rawPassword = "Abcdefg1";
        String email = "dup_" + UUID.randomUUID().toString().substring(0,6) + "@example.com";
        String user1 = "u1_" + UUID.randomUUID().toString().substring(0,6);
        String user2 = "u2_" + UUID.randomUUID().toString().substring(0,6);

        authService.register(user1, email, rawPassword);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.register(user2, email, rawPassword),
                "Expected duplicate email to throw IllegalArgumentException");
        assertTrue(ex.getMessage().toLowerCase().contains("email"));
    }

    @Test
    @DisplayName("Weak password is rejected (backend policy matches frontend)")
    void weakPasswordRejected() {
        String weak = "abc123"; // too short, lacks upper
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.register("wk_" + UUID.randomUUID().toString().substring(0,5),
                        "weak_" + UUID.randomUUID().toString().substring(0,5) + "@example.com", weak));
        assertTrue(ex.getMessage().toLowerCase().contains("password"));
    }

    @Test
    @DisplayName("Duplicate email differing only by case is rejected")
    @Transactional
    void duplicateEmailDifferentCaseRejected() {
        String emailLower = "case_" + UUID.randomUUID().toString().substring(0,6) + "@example.com";
        String emailUpperVariant = emailLower.toUpperCase();
        String password = "Abcdefg1";
        authService.register("c1_" + UUID.randomUUID().toString().substring(0,5), emailLower, password);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.register("c2_" + UUID.randomUUID().toString().substring(0,5), emailUpperVariant, password));
        assertTrue(ex.getMessage().toLowerCase().contains("email"));
    }
}
