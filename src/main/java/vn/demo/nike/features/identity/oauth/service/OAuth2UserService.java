package vn.demo.nike.features.identity.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.identity.oauth.entity.OAuthProviderAccount;
import vn.demo.nike.features.identity.oauth.repository.OAuthProviderAccountRepository;
import vn.demo.nike.features.identity.user.entity.User;
import vn.demo.nike.features.identity.user.enums.Role;
import vn.demo.nike.features.identity.user.repository.UserRepository;

import java.security.AuthProvider;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OAuthProviderAccountRepository oAuthProviderAccountRepository;

    private User provision(
            AuthProvider provider,
            String providerSubject,
            String email,
            boolean emailVerified,
            String displayName
    ) {
        validateOAuthData(providerSubject, email, emailVerified);

        return oAuthProviderAccountRepository
                .findByProviderAndProviderSubject(provider.getName(), providerSubject)
                .map(account -> requireActive(account.getUser()))
                .map(this::requireActive)
                .orElseGet(() ->
                        linkOrCreateUser(
                                provider,
                                providerSubject,
                                email,
                                displayName
                        )
                );
    }

    private User linkOrCreateUser(
            AuthProvider provider,
            String providerSubject,
            String email,
            String displayName
    ) {
        User user = userRepository.findByEmail(email)
                .map(this::requireActive)
                .orElseGet(() -> createUser(email, displayName));

        OAuthProviderAccount account = new OAuthProviderAccount();

        account.setUser(user);
        account.setProvider(provider.getName());
        account.setProviderSubject(providerSubject);
        account.setEmailAtLinkItem(email);
        oAuthProviderAccountRepository.save(account);

        return user;
    }

    private void validateOAuthData(
            String providerSubject,
            String email,
            boolean emailVerified
    ) {
        if(providerSubject == null || providerSubject.isBlank()) {
            throw new IllegalArgumentException("Provider subject is required");
        }

        if(email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if(!emailVerified) {
            throw new IllegalArgumentException("Email must be verified");
        }
    }

    private User createUser(String email, String displayName) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(generateUsername(email, displayName));
        user.setPassword(
                passwordEncoder.encode(
                        UUID.randomUUID().toString()
                )
        );
        user.setRole(Role.USER);
        user.setEnabled(true);
        user.setLocked(false);
        return userRepository.save(user);
    }

    private User requireActive(User user) {
        if(!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        }

        if(!user.isAccountNonLocked()) {
            throw new LockedException("User is locked");
        }

        return user;
    }

    private String generateUsername(
            String email,
            String displayName
    ) {

        String base;

        if (displayName == null || displayName.isBlank()) {
            base = email.substring(0, email.indexOf('@'));
        } else {
            base = displayName;
        }

        base = base.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]", "");

        if (base.isBlank()) {
            base = "user";
        }

        String candidate = base;
        int suffix = 1;

        while (userRepository.existsByUsername(candidate)) {
            candidate = base + suffix++;
        }

        return candidate;
    }
}
