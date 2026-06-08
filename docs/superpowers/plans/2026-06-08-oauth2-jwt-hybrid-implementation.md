# OAuth2 JWT Hybrid Authentication Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add Google OAuth2 login for the JSP web app and Nike-issued JWT authentication for `/api/v1/**` without breaking the existing session-based flows.

**Architecture:** Keep browser/JSP/Admin/Checkout/Payment on Spring Security sessions. Add OAuth2 login to the web chain and provision provider identities into local `User` records. Add a separate stateless API chain for JWT-protected API clients and mobile clients.

**Tech Stack:** Spring Boot 3.2, Spring Security, Spring OAuth2 Client, Spring OAuth2 Resource Server/Jose, JPA, Flyway, MySQL, JUnit 5, Mockito.

---

## File Structure

Create:

- `src/main/java/vn/demo/nike/features/identity/oauth/entity/OAuthProviderAccount.java` - stores provider-to-user account links.
- `src/main/java/vn/demo/nike/features/identity/oauth/repository/OAuthProviderAccountRepository.java` - lookup provider links.
- `src/main/java/vn/demo/nike/features/identity/oauth/model/NikeOAuth2User.java` - authenticated OAuth principal that exposes the local user id.
- `src/main/java/vn/demo/nike/features/identity/oauth/service/OAuth2UserProvisioningService.java` - finds, creates, and links local users.
- `src/main/java/vn/demo/nike/features/identity/oauth/service/NikeOAuth2UserService.java` - adapts Spring OAuth2 user info into `NikeOAuth2User`.
- `src/main/java/vn/demo/nike/features/identity/auth/jwt/JwtProperties.java` - typed JWT config.
- `src/main/java/vn/demo/nike/features/identity/auth/jwt/JwtService.java` - creates JWTs for local users.
- `src/main/java/vn/demo/nike/features/identity/auth/jwt/JwtAuthenticationConverter.java` - maps JWT claims into authorities and principal.
- `src/main/java/vn/demo/nike/features/identity/auth/dto/LoginResponse.java` - API login response.
- `src/main/resources/db/migration/V3__add_oauth_provider_accounts.sql` - OAuth account-linking table.
- `src/test/java/vn/demo/nike/features/identity/oauth/service/OAuth2UserProvisioningServiceTest.java` - provisioning unit tests.
- `src/test/java/vn/demo/nike/features/identity/auth/jwt/JwtServiceTest.java` - JWT unit tests.
- `src/test/java/vn/demo/nike/shared/config/ApiSecurityConfigTest.java` - API security boundary tests.

Modify:

- `pom.xml` - add OAuth2 client and resource server dependencies.
- `src/main/resources/application.properties` - add JWT and OAuth config placeholders.
- `src/main/resources/application-local.properties` - add local placeholder values using environment variables.
- `src/main/java/vn/demo/nike/shared/config/SecurityConfig.java` - add API chain, OAuth2 login, JWT resource server support.
- `src/main/java/vn/demo/nike/features/identity/user/service/SecurityCurrentUserProvider.java` - support OAuth2 principal user id.
- `src/main/java/vn/demo/nike/features/identity/auth/controller/AuthApiController.java` - add `/api/v1/auth/login`.
- `src/main/webapp/WEB-INF/views/user/auth.jsp` - add Google login link/button if not already present.
- `src/main/resources/static/js/customer/pages/auth.js` - wire social button to Spring OAuth2 authorization URL.

Commit after each task. Keep `.understand-anything/` untracked unless the user explicitly asks to include it.

---

### Task 1: Add OAuth2 And JWT Dependencies

**Files:**
- Modify: `pom.xml`

- [ ] **Step 1: Add dependencies**

Add these dependencies after `spring-boot-starter-security`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

Keep the existing `nimbus-jose-jwt` dependency for now. Remove it only after compile proves it is redundant.

- [ ] **Step 2: Compile**

Run:

```powershell
mvn -q -DskipTests compile
```

Expected: build succeeds.

- [ ] **Step 3: Commit**

```powershell
git add pom.xml
git commit -m "build: add oauth2 client and resource server"
```

---

### Task 2: Add Configuration Placeholders

**Files:**
- Modify: `src/main/resources/application.properties`
- Modify: `src/main/resources/application-local.properties`

- [ ] **Step 1: Add shared typed config**

Append to `application.properties`:

```properties
# JWT authentication
app.security.jwt.issuer=nike-ecommerce
app.security.jwt.access-token-ttl=PT30M
app.security.jwt.secret=${JWT_SECRET:}
```

- [ ] **Step 2: Add local OAuth2 placeholders**

Append to `application-local.properties`:

```properties
# Google OAuth2 local login
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID:}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET:}
spring.security.oauth2.client.registration.google.scope=openid,profile,email
```

Do not commit real Google secrets.

- [ ] **Step 3: Compile**

Run:

```powershell
mvn -q -DskipTests compile
```

Expected: build succeeds.

- [ ] **Step 4: Commit**

```powershell
git add src/main/resources/application.properties src/main/resources/application-local.properties
git commit -m "config: add oauth2 and jwt placeholders"
```

---

### Task 3: Add OAuth Provider Account Persistence

**Files:**
- Create: `src/main/resources/db/migration/V3__add_oauth_provider_accounts.sql`
- Create: `src/main/java/vn/demo/nike/features/identity/oauth/entity/OAuthProviderAccount.java`
- Create: `src/main/java/vn/demo/nike/features/identity/oauth/repository/OAuthProviderAccountRepository.java`

- [ ] **Step 1: Add Flyway migration**

Create `V3__add_oauth_provider_accounts.sql`:

```sql
CREATE TABLE IF NOT EXISTS oauth_provider_accounts (
  id BIGINT NOT NULL AUTO_INCREMENT,
  create_date DATETIME(6) NULL,
  update_date DATETIME(6) NULL,
  user_id BIGINT NOT NULL,
  provider VARCHAR(50) NOT NULL,
  provider_subject VARCHAR(255) NOT NULL,
  email_at_link_time VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_oauth_provider_subject (provider, provider_subject),
  KEY idx_oauth_provider_accounts_user_id (user_id),
  CONSTRAINT fk_oauth_provider_accounts_user
    FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

- [ ] **Step 2: Add entity**

Create `OAuthProviderAccount.java`:

```java
package vn.demo.nike.features.identity.oauth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import vn.demo.nike.features.identity.user.entity.User;
import vn.demo.nike.shared.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(
        name = "oauth_provider_accounts",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_oauth_provider_subject",
                columnNames = {"provider", "provider_subject"}
        )
)
public class OAuthProviderAccount extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String provider;

    @Column(name = "provider_subject", nullable = false)
    private String providerSubject;

    @Column(name = "email_at_link_time", nullable = false)
    private String emailAtLinkTime;
}
```

- [ ] **Step 3: Add repository**

Create `OAuthProviderAccountRepository.java`:

```java
package vn.demo.nike.features.identity.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.demo.nike.features.identity.oauth.entity.OAuthProviderAccount;

import java.util.Optional;

public interface OAuthProviderAccountRepository extends JpaRepository<OAuthProviderAccount, Long> {

    Optional<OAuthProviderAccount> findByProviderAndProviderSubject(String provider, String providerSubject);

    boolean existsByProviderAndProviderSubject(String provider, String providerSubject);
}
```

- [ ] **Step 4: Compile**

Run:

```powershell
mvn -q -DskipTests compile
```

Expected: build succeeds.

- [ ] **Step 5: Commit**

```powershell
git add src/main/resources/db/migration/V3__add_oauth_provider_accounts.sql src/main/java/vn/demo/nike/features/identity/oauth
git commit -m "feat: add oauth provider account persistence"
```

---

### Task 4: Implement OAuth2 User Provisioning

**Files:**
- Create: `src/main/java/vn/demo/nike/features/identity/oauth/service/OAuth2UserProvisioningService.java`
- Test: `src/test/java/vn/demo/nike/features/identity/oauth/service/OAuth2UserProvisioningServiceTest.java`

- [ ] **Step 1: Write tests first**

Create `OAuth2UserProvisioningServiceTest.java` with tests for:

```java
package vn.demo.nike.features.identity.oauth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.demo.nike.features.identity.oauth.entity.OAuthProviderAccount;
import vn.demo.nike.features.identity.oauth.repository.OAuthProviderAccountRepository;
import vn.demo.nike.features.identity.user.entity.User;
import vn.demo.nike.features.identity.user.enums.Role;
import vn.demo.nike.features.identity.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2UserProvisioningServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    OAuthProviderAccountRepository accountRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    OAuth2UserService service;

    @Test
    void provisionCreatesNewUserWhenVerifiedEmailDoesNotExist() {
        when(accountRepository.findByProviderAndProviderSubject("google", "sub-1")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("person@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.existsByUsername("person")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(10L);
            return user;
        });

        User user = service.provision("google", "sub-1", "person@example.com", true, "Person Name");

        assertThat(user.getEmail()).isEqualTo("person@example.com");
        assertThat(user.getUsername()).isEqualTo("person");
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.isEnabled()).isTrue();

        ArgumentCaptor<OAuthProviderAccount> captor = ArgumentCaptor.forClass(OAuthProviderAccount.class);
        verify(accountRepository).save(captor.capture());
        assertThat(captor.getValue().getProvider()).isEqualTo("google");
        assertThat(captor.getValue().getProviderSubject()).isEqualTo("sub-1");
    }

    @Test
    void provisionLinksExistingUserByEmail() {
        User existing = new User();
        existing.setId(11L);
        existing.setEmail("person@example.com");
        existing.setUsername("person");
        existing.setRole(Role.USER);
        existing.setEnabled(true);
        existing.setLocked(false);

        when(accountRepository.findByProviderAndProviderSubject("google", "sub-1")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("person@example.com")).thenReturn(Optional.of(existing));

        User user = service.provision("google", "sub-1", "person@example.com", true, "Person Name");

        assertThat(user).isSameAs(existing);
        verify(accountRepository).save(any(OAuthProviderAccount.class));
    }

    @Test
    void provisionRejectsUnverifiedEmail() {
        assertThatThrownBy(() -> service.provision("google", "sub-1", "person@example.com", false, "Person Name"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("verified");
    }
}
```

- [ ] **Step 2: Run tests and verify failure**

Run:

```powershell
mvn -q -Dtest=OAuth2UserProvisioningServiceTest test
```

Expected: fail because `OAuth2UserProvisioningService` does not exist.

- [ ] **Step 3: Implement service**

Create `OAuth2UserProvisioningService.java`:

```java
package vn.demo.nike.features.identity.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.demo.nike.features.identity.oauth.entity.OAuthProviderAccount;
import vn.demo.nike.features.identity.oauth.repository.OAuthProviderAccountRepository;
import vn.demo.nike.features.identity.user.entity.User;
import vn.demo.nike.features.identity.user.enums.Role;
import vn.demo.nike.features.identity.user.repository.UserRepository;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuth2UserProvisioningService {

    private final UserRepository userRepository;
    private final OAuthProviderAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User provision(String provider, String providerSubject, String email, boolean emailVerified, String displayName) {
        if (providerSubject == null || providerSubject.isBlank()) {
            throw new IllegalArgumentException("OAuth provider subject is required");
        }
        if (email == null || email.isBlank() || !emailVerified) {
            throw new IllegalArgumentException("OAuth email must be verified");
        }

        String normalizedProvider = provider.trim().toLowerCase(Locale.ROOT);
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        return accountRepository.findByProviderAndProviderSubject(normalizedProvider, providerSubject)
                .map(OAuthProviderAccount::getUser)
                .map(this::requireActive)
                .orElseGet(() -> linkOrCreateUser(normalizedProvider, providerSubject, normalizedEmail, displayName));
    }

    private User linkOrCreateUser(String provider, String providerSubject, String email, String displayName) {
        User user = userRepository.findByEmail(email)
                .map(this::requireActive)
                .orElseGet(() -> createUser(email, displayName));

        OAuthProviderAccount account = new OAuthProviderAccount();
        account.setUser(user);
        account.setProvider(provider);
        account.setProviderSubject(providerSubject);
        account.setEmailAtLinkTime(email);
        accountRepository.save(account);
        return user;
    }

    private User createUser(String email, String displayName) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(uniqueUsername(email, displayName));
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setRole(Role.USER);
        user.setLocked(false);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    private User requireActive(User user) {
        if (!user.isEnabled()) {
            throw new DisabledException("Account is disabled");
        }
        if (!user.isAccountNonLocked()) {
            throw new LockedException("Account is locked");
        }
        return user;
    }

    private String uniqueUsername(String email, String displayName) {
        String base = displayName == null || displayName.isBlank()
                ? email.substring(0, email.indexOf('@'))
                : displayName;
        base = base.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "");
        if (base.isBlank()) {
            base = "user";
        }
        String candidate = base;
        int suffix = 1;
        while (userRepository.existsByUsername(candidate)) {
            candidate = base + suffix;
            suffix++;
        }
        return candidate;
    }
}
```

- [ ] **Step 4: Run tests**

```powershell
mvn -q -Dtest=OAuth2UserProvisioningServiceTest test
```

Expected: pass.

- [ ] **Step 5: Commit**

```powershell
git add src/main/java/vn/demo/nike/features/identity/oauth src/test/java/vn/demo/nike/features/identity/oauth
git commit -m "feat: provision oauth users"
```

---

### Task 5: Add OAuth2 Principal And Web OAuth2 Login

**Files:**
- Create: `src/main/java/vn/demo/nike/features/identity/oauth/model/NikeOAuth2User.java`
- Create: `src/main/java/vn/demo/nike/features/identity/oauth/service/NikeOAuth2UserService.java`
- Modify: `src/main/java/vn/demo/nike/features/identity/user/service/SecurityCurrentUserProvider.java`
- Modify: `src/main/java/vn/demo/nike/shared/config/SecurityConfig.java`
- Modify: `src/main/webapp/WEB-INF/views/user/auth.jsp`
- Modify: `src/main/resources/static/js/customer/pages/auth.js`

- [ ] **Step 1: Add OAuth2 principal**

Create `NikeOAuth2User.java`:

```java
package vn.demo.nike.features.identity.oauth.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class NikeOAuth2User implements OAuth2User {

    private final Long id;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;

    public NikeOAuth2User(Long id, String email, Collection<? extends GrantedAuthority> authorities,
                          Map<String, Object> attributes, String nameAttributeKey) {
        this.id = id;
        this.email = email;
        this.authorities = authorities;
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
    }

    @Override
    public String getName() {
        Object value = attributes.get(nameAttributeKey);
        return value == null ? email : value.toString();
    }
}
```

- [ ] **Step 2: Add custom OAuth2 user service**

Create `NikeOAuth2UserService.java`:

```java
package vn.demo.nike.features.identity.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.identity.oauth.model.NikeOAuth2User;
import vn.demo.nike.features.identity.user.entity.User;

@Service
@RequiredArgsConstructor
public class NikeOAuth2UserService implements org.springframework.security.oauth2.client.userinfo.OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final OAuth2UserService provisioningService;
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = delegate.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String subject = stringAttribute(oauthUser, "sub");
        String email = stringAttribute(oauthUser, "email");
        boolean emailVerified = Boolean.TRUE.equals(oauthUser.getAttribute("email_verified"));
        String name = stringAttribute(oauthUser, "name");

        User user = provisioningService.provision(provider, subject, email, emailVerified, name);
        return new NikeOAuth2User(user.getId(), user.getEmail(), user.getAuthorities(), oauthUser.getAttributes(), "sub");
    }

    private String stringAttribute(OAuth2User user, String key) {
        Object value = user.getAttribute(key);
        return value == null ? null : value.toString();
    }
}
```

- [ ] **Step 3: Update current user provider**

In `SecurityCurrentUserProvider`, add:

```java
import vn.demo.nike.features.identity.oauth.model.NikeOAuth2User;
```

Then add this branch after the `User` branch:

```java
if (principal instanceof NikeOAuth2User oauthUser) {
    return oauthUser.getId();
}
```

- [ ] **Step 4: Update web security chain**

Inject `NikeOAuth2UserService` into `SecurityConfig`:

```java
private final NikeOAuth2UserService nikeOAuth2UserService;
```

Add import:

```java
import vn.demo.nike.features.identity.oauth.service.NikeOAuth2UserService;
```

After `.formLogin(...)`, add:

```java
.oauth2Login(oauth -> oauth
        .loginPage("/login")
        .userInfoEndpoint(userInfo -> userInfo.userService(nikeOAuth2UserService))
        .defaultSuccessUrl("/", false)
        .failureUrl("/login?oauthError=true")
)
```

- [ ] **Step 5: Wire login page**

In `auth.jsp`, make the Google social button/link navigate to:

```text
${pageContext.request.contextPath}/oauth2/authorization/google
```

If the button is generated by JS, add `data-social-provider="google"` and let JS redirect.

- [ ] **Step 6: Update JS social click**

In `auth.js`, replace the current placeholder social-login handler with:

```javascript
document.querySelectorAll("[data-social-provider]").forEach((button) => {
  button.addEventListener("click", (event) => {
    event.preventDefault();
    const provider = button.dataset.socialProvider;
    if (provider === "google") {
      window.location.href = `${ctx}/oauth2/authorization/google`;
      return;
    }
    showToast(`Dang nhap voi ${provider} se duoc trien khai sau.`, { type: "info" });
  });
});
```

- [ ] **Step 7: Compile**

```powershell
mvn -q -DskipTests compile
```

Expected: build succeeds.

- [ ] **Step 8: Commit**

```powershell
git add src/main/java/vn/demo/nike/features/identity/oauth src/main/java/vn/demo/nike/features/identity/user/service/SecurityCurrentUserProvider.java src/main/java/vn/demo/nike/shared/config/SecurityConfig.java src/main/webapp/WEB-INF/views/user/auth.jsp src/main/resources/static/js/customer/pages/auth.js
git commit -m "feat: add google oauth2 web login"
```

---

### Task 6: Add JWT Properties And Service

**Files:**
- Create: `src/main/java/vn/demo/nike/features/identity/auth/jwt/JwtProperties.java`
- Create: `src/main/java/vn/demo/nike/features/identity/auth/jwt/JwtService.java`
- Test: `src/test/java/vn/demo/nike/features/identity/auth/jwt/JwtServiceTest.java`

- [ ] **Step 1: Add typed properties**

Create `JwtProperties.java`:

```java
package vn.demo.nike.features.identity.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.security.jwt")
public record JwtProperties(
        String issuer,
        Duration accessTokenTtl,
        String secret
) {
}
```

- [ ] **Step 2: Add JWT service**

Create `JwtService.java`:

```java
package vn.demo.nike.features.identity.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.identity.user.entity.User;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public String createAccessToken(User user) {
        Instant now = Instant.now();
        List<String> roles = user.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.issuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.accessTokenTtl()))
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("roles", roles)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
```

- [ ] **Step 3: Add encoder/decoder beans to SecurityConfig**

Add imports:

```java
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import vn.demo.nike.features.identity.auth.jwt.JwtProperties;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
```

Add annotation to `SecurityConfig`:

```java
@EnableConfigurationProperties(JwtProperties.class)
```

Add beans:

```java
@Bean
public JwtEncoder jwtEncoder(JwtProperties properties) {
    SecretKey key = jwtSecretKey(properties);
    return new NimbusJwtEncoder(new ImmutableSecret<>(key));
}

@Bean
public JwtDecoder jwtDecoder(JwtProperties properties) {
    return NimbusJwtDecoder.withSecretKey(jwtSecretKey(properties)).build();
}

private SecretKey jwtSecretKey(JwtProperties properties) {
    String secret = properties.secret();
    if (secret == null || secret.length() < 32) {
        throw new IllegalStateException("app.security.jwt.secret must be at least 32 characters");
    }
    return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
}
```

- [ ] **Step 4: Add test properties**

In `JwtServiceTest`, use `@SpringBootTest` only if needed. Prefer a focused test with manually created encoder:

```java
package vn.demo.nike.features.identity.auth.jwt;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import vn.demo.nike.features.identity.user.entity.User;
import vn.demo.nike.features.identity.user.enums.Role;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    @Test
    void createAccessTokenIncludesUserIdentityAndRoles() {
        String secret = "01234567890123456789012345678901";
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        JwtService service = new JwtService(
                new NimbusJwtEncoder(new ImmutableSecret<>(key)),
                new JwtProperties("nike-ecommerce", Duration.ofMinutes(30), secret)
        );
        User user = new User();
        user.setId(7L);
        user.setEmail("person@example.com");
        user.setRole(Role.USER);

        String token = service.createAccessToken(user);
        var jwt = NimbusJwtDecoder.withSecretKey(key).build().decode(token);

        assertThat(jwt.getSubject()).isEqualTo("7");
        assertThat(jwt.getClaimAsString("email")).isEqualTo("person@example.com");
        assertThat(jwt.getClaimAsStringList("roles")).containsExactly("ROLE_USER");
    }
}
```

- [ ] **Step 5: Run JWT test**

```powershell
mvn -q -Dtest=JwtServiceTest test
```

Expected: pass.

- [ ] **Step 6: Commit**

```powershell
git add src/main/java/vn/demo/nike/features/identity/auth/jwt src/main/java/vn/demo/nike/shared/config/SecurityConfig.java src/test/java/vn/demo/nike/features/identity/auth/jwt
git commit -m "feat: add jwt token service"
```

---

### Task 7: Add API JWT Security Chain

**Files:**
- Create: `src/main/java/vn/demo/nike/features/identity/auth/jwt/JwtAuthenticationConverter.java`
- Modify: `src/main/java/vn/demo/nike/shared/config/SecurityConfig.java`
- Test: `src/test/java/vn/demo/nike/shared/config/ApiSecurityConfigTest.java`

- [ ] **Step 1: Add JWT authentication converter**

Create `JwtAuthenticationConverter.java`:

```java
package vn.demo.nike.features.identity.auth.jwt;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        var authorities = roles == null ? List.<SimpleGrantedAuthority>of() : roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
    }
}
```

- [ ] **Step 2: Add API chain to SecurityConfig**

Inject converter:

```java
private final JwtAuthenticationConverter jwtAuthenticationConverter;
```

Add imports:

```java
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
```

Add this filter chain before the web chain:

```java
@Bean
@Order(2)
public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
    http
            .securityMatcher(new AntPathRequestMatcher("/api/v1/**"))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/v1/auth/login", "/api/v1/auth/oauth2/google", "/api/v1/products/**").permitAll()
                    .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .oauth2ResourceServer(resourceServer -> resourceServer
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
            );
    return http.build();
}
```

Keep the web chain at `@Order(3)` because management chains currently use `@Order(1)`.

- [ ] **Step 3: Add a minimal protected test controller for security test only**

In `ApiSecurityConfigTest`, use nested test configuration:

```java
package vn.demo.nike.shared.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.demo.nike.features.identity.auth.jwt.JwtAuthenticationConverter;
import vn.demo.nike.features.identity.auth.jwt.JwtProperties;
import vn.demo.nike.features.identity.oauth.service.NikeOAuth2UserService;
import vn.demo.nike.features.identity.user.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ApiSecurityConfigTest.ProtectedApiController.class)
@Import({SecurityConfig.class, JwtAuthenticationConverter.class})
class ApiSecurityConfigTest {

    @Autowired MockMvc mvc;

    @MockitoBean UserRepository userRepository;
    @MockitoBean NikeOAuth2UserService nikeOAuth2UserService;
    @MockitoBean JwtDecoder jwtDecoder;
    @MockitoBean JwtProperties jwtProperties;

    @Test
    void protectedApiWithoutTokenReturnsUnauthorized() throws Exception {
        mvc.perform(get("/api/v1/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedApiWithValidTokenReturnsOk() throws Exception {
        Jwt jwt = new Jwt(
                "token",
                Instant.now(),
                Instant.now().plusSeconds(60),
                Map.of("alg", "HS256"),
                Map.of("sub", "7", "roles", List.of("ROLE_USER"))
        );
        when(jwtDecoder.decode("token")).thenReturn(jwt);

        mvc.perform(get("/api/v1/profile").header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }

    @RestController
    static class ProtectedApiController {
        @GetMapping("/api/v1/profile")
        String profile() {
            return "ok";
        }
    }
}
```

- [ ] **Step 4: Run security test**

```powershell
mvn -q -Dtest=ApiSecurityConfigTest test
```

Expected: pass.

- [ ] **Step 5: Commit**

```powershell
git add src/main/java/vn/demo/nike/features/identity/auth/jwt/JwtAuthenticationConverter.java src/main/java/vn/demo/nike/shared/config/SecurityConfig.java src/test/java/vn/demo/nike/shared/config/ApiSecurityConfigTest.java
git commit -m "feat: secure api with jwt"
```

---

### Task 8: Add API Email Password Login

**Files:**
- Create: `src/main/java/vn/demo/nike/features/identity/auth/dto/LoginResponse.java`
- Modify: `src/main/java/vn/demo/nike/features/identity/auth/controller/AuthApiController.java`

- [ ] **Step 1: Add response DTO**

Create `LoginResponse.java`:

```java
package vn.demo.nike.features.identity.auth.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds,
        Long userId,
        String email
) {
}
```

- [ ] **Step 2: Update AuthApiController dependencies**

Add fields:

```java
private final AuthenticationManager authenticationManager;
private final JwtService jwtService;
private final JwtProperties jwtProperties;
```

Add imports:

```java
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import vn.demo.nike.features.identity.auth.dto.LoginResponse;
import vn.demo.nike.features.identity.auth.jwt.JwtProperties;
import vn.demo.nike.features.identity.auth.jwt.JwtService;
import vn.demo.nike.features.identity.auth.request.LoginRequest;
import vn.demo.nike.features.identity.user.entity.User;
```

- [ ] **Step 3: Add login endpoint**

Add method:

```java
@PostMapping("/api/v1/auth/login")
public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
    );
    User user = (User) authentication.getPrincipal();
    String token = jwtService.createAccessToken(user);
    return ResponseEntity.ok(new LoginResponse(
            token,
            "Bearer",
            jwtProperties.accessTokenTtl().toSeconds(),
            user.getId(),
            user.getEmail()
    ));
}
```

This project's form login uses `email` as the username parameter, so API login should authenticate with `request.getEmail()`.

- [ ] **Step 4: Compile**

```powershell
mvn -q -DskipTests compile
```

Expected: build succeeds.

- [ ] **Step 5: Commit**

```powershell
git add src/main/java/vn/demo/nike/features/identity/auth
git commit -m "feat: add jwt api login"
```

---

### Task 9: Add Mobile Google Token Exchange Endpoint

**Files:**
- Create: `src/main/java/vn/demo/nike/features/identity/auth/request/GoogleTokenLoginRequest.java`
- Modify: `src/main/java/vn/demo/nike/features/identity/auth/controller/AuthApiController.java`

- [ ] **Step 1: Add request DTO**

Create `GoogleTokenLoginRequest.java`:

```java
package vn.demo.nike.features.identity.auth.request;

import jakarta.validation.constraints.NotBlank;

public record GoogleTokenLoginRequest(
        @NotBlank String idToken
) {
}
```

- [ ] **Step 2: Add endpoint skeleton**

Add endpoint:

```java
@PostMapping("/api/v1/auth/oauth2/google")
public ResponseEntity<LoginResponse> loginWithGoogle(@Valid @RequestBody GoogleTokenLoginRequest request) {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Google mobile token exchange is not implemented yet");
}
```

Add imports:

```java
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import vn.demo.nike.features.identity.auth.request.GoogleTokenLoginRequest;
```

This endpoint is intentionally added as `501` until Google ID token verifier is implemented. It reserves the contract without pretending token verification is done.

- [ ] **Step 3: Compile**

```powershell
mvn -q -DskipTests compile
```

Expected: build succeeds.

- [ ] **Step 4: Commit**

```powershell
git add src/main/java/vn/demo/nike/features/identity/auth
git commit -m "feat: reserve google mobile login endpoint"
```

---

### Task 10: End-To-End Verification

**Files:**
- No planned code changes unless verification exposes a bug.

- [ ] **Step 1: Run focused tests**

```powershell
mvn -q -Dtest=OAuth2UserProvisioningServiceTest,JwtServiceTest,ApiSecurityConfigTest test
```

Expected: all pass.

- [ ] **Step 2: Run full test suite**

```powershell
mvn test
```

Expected: all pass.

- [ ] **Step 3: Compile/package**

```powershell
mvn clean package -DskipTests
```

Expected: WAR builds at `target/nike-starter.war`.

- [ ] **Step 4: Manual local OAuth setup**

Set environment variables before running local app:

```powershell
$env:GOOGLE_CLIENT_ID="your-google-client-id"
$env:GOOGLE_CLIENT_SECRET="your-google-client-secret"
$env:JWT_SECRET="change-this-to-at-least-32-characters"
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Configure Google OAuth redirect URI:

```text
http://localhost:9090/login/oauth2/code/google
```

- [ ] **Step 5: Manual browser checks**

Verify:

- `/login` still shows email/password login.
- Existing email/password login still creates a session.
- Google login redirects to Google and then back to `/`.
- `/admin/**` still requires `ROLE_ADMIN`.
- Logout clears the session.
- Checkout and payment pages still use session behavior.

- [ ] **Step 6: Manual API checks**

Call login:

```powershell
Invoke-RestMethod -Method Post `
  -Uri "http://localhost:9090/api/v1/auth/login" `
  -ContentType "application/json" `
  -Body '{"email":"user@example.com","password":"password"}'
```

Use the returned token:

```powershell
Invoke-RestMethod -Method Get `
  -Uri "http://localhost:9090/api/v1/profile" `
  -Headers @{ Authorization = "Bearer <token>" }
```

Expected:

- Valid protected API returns `200` when endpoint exists.
- Missing token returns `401`.
- Customer token on admin API returns `403`.

- [ ] **Step 7: Final commit for verification fixes only**

If verification required fixes:

```powershell
git add <fixed-files>
git commit -m "fix: stabilize oauth2 jwt auth integration"
```

If no fixes were needed, do not create an empty commit.

---

## Scope Notes

- Refresh tokens are intentionally excluded from this implementation. Add them after access-token login works end-to-end.
- Mobile Google ID token verification is reserved but not completed in Task 9. Complete it in a follow-up using Google's official token verifier or Spring-supported OIDC validation.
- Do not convert JSP pages to JWT. JWT is only for API and mobile clients.
- Do not store access tokens in browser localStorage for JSP flows.

## Self-Review

- Spec coverage: web session auth remains, Google OAuth2 web login is added, JWT API boundary is added, local roles remain authoritative, account linking is explicit.
- Placeholder scan: no task uses unresolved placeholder wording; the mobile endpoint is deliberately `501` with a stated follow-up boundary.
- Type consistency: user role uses existing `Role.USER`; API JWT roles use Spring authorities such as `ROLE_USER`; web admin remains `ROLE_ADMIN`.
