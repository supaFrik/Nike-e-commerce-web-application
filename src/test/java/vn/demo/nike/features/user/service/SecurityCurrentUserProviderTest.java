package vn.demo.nike.features.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityCurrentUserProviderTest {

    private final SecurityCurrentUserProvider currentUserProvider = new SecurityCurrentUserProvider();

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void returnsCurrentUserIdFromJwtSubject() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "HS256")
                .issuer("nike-ecommerce")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .subject("42")
                .claims(claims -> claims.putAll(Map.of("email", "person@example.com")))
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new JwtAuthenticationToken(jwt, List.of(new SimpleGrantedAuthority("ROLE_USER")), jwt.getSubject())
        );

        assertThat(currentUserProvider.getCurrentUserId()).isEqualTo(42L);
    }
}
