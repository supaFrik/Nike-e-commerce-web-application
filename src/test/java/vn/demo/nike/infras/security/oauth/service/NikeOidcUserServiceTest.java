package vn.demo.nike.infras.security.oauth.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import vn.demo.nike.features.user.entity.User;
import vn.demo.nike.features.user.enums.Role;
import vn.demo.nike.infras.security.oauth.entity.NikeOidcUser;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NikeOidcUserServiceTest {

    @Test
    void provisionsGoogleOidcUserAndUsesDisplayName() {
        OidcUserService delegate = Mockito.mock(OidcUserService.class);
        OAuth2UserProvisioningService provisioningService = Mockito.mock(OAuth2UserProvisioningService.class);
        NikeOidcUserService service = new NikeOidcUserService(provisioningService, delegate);

        OidcUserRequest request = oidcUserRequest();
        OidcUser googleUser = oidcUser("google-sub-123", "person@example.com", "Google Person", true);
        when(delegate.loadUser(request)).thenReturn(googleUser);

        User appUser = new User();
        appUser.setId(42L);
        appUser.setEmail("person@example.com");
        appUser.setUsername("googleperson");
        appUser.setRole(Role.USER);
        when(provisioningService.provision("google", "google-sub-123", "person@example.com", true, "Google Person"))
                .thenReturn(appUser);

        OidcUser result = service.loadUser(request);

        assertThat(result).isInstanceOf(NikeOidcUser.class);
        assertThat(result.getName()).isEqualTo("Google Person");
        assertThat(((NikeOidcUser) result).getId()).isEqualTo(42L);
        verify(provisioningService).provision("google", "google-sub-123", "person@example.com", true, "Google Person");
    }

    private OidcUserRequest oidcUserRequest() {
        ClientRegistration registration = ClientRegistration.withRegistrationId("google")
                .clientId("client")
                .clientSecret("secret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost/login/oauth2/code/google")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .issuerUri("https://accounts.google.com")
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .userInfoUri("https://openidconnect.googleapis.com/v1/userinfo")
                .userNameAttributeName("sub")
                .scope("openid", "email", "profile")
                .build();

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                "token",
                Instant.now(),
                Instant.now().plusSeconds(300)
        );

        return new OidcUserRequest(registration, accessToken, idToken("google-sub-123", "person@example.com", "Google Person", true));
    }

    private OidcIdToken idToken(String subject, String email, String name, boolean emailVerified) {
        return new OidcIdToken(
                "id-token",
                Instant.now(),
                Instant.now().plusSeconds(300),
                Map.of(
                        "sub", subject,
                        "email", email,
                        "email_verified", emailVerified,
                        "name", name
                )
        );
    }

    private OidcUser oidcUser(String subject, String email, String name, boolean emailVerified) {
        OidcIdToken idToken = idToken(subject, email, name, emailVerified);
        return new DefaultOidcUser(List.of(new SimpleGrantedAuthority("ROLE_USER")), idToken, "sub");
    }
}
