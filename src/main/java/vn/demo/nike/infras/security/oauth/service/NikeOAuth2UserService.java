package vn.demo.nike.infras.security.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.user.entity.User;
import vn.demo.nike.infras.security.oauth.entity.NikeOAuth2User;

@Service
@RequiredArgsConstructor
public class NikeOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final OAuth2UserProvisioningService oAuth2UserProvisioningService;
    private final DefaultOAuth2UserService delegate =
            new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oauth2User = delegate.loadUser(userRequest);

        String provider =
                userRequest.getClientRegistration().getRegistrationId();

        String subject = stringAttribute(oauth2User, "sub");
        String email = stringAttribute(oauth2User, "email");

        boolean emailVerified =
                Boolean.TRUE.equals(
                        oauth2User.getAttributes().get("email_verified")
                );

        String name = stringAttribute(oauth2User, "name");

        User user = oAuth2UserProvisioningService.provision(
                provider,
                subject,
                email,
                emailVerified,
                name
        );

        return new NikeOAuth2User(
                user.getId(),
                user.getEmail(),
                resolveDisplayName(user, name),
                user.getAuthorities(),
                oauth2User.getAttributes()
        );
    }

    private String resolveDisplayName(User user, String oauthDisplayName) {
        if (oauthDisplayName != null && !oauthDisplayName.isBlank()) {
            return oauthDisplayName;
        }
        if (user.getUsername() != null && !user.getUsername().isBlank()) {
            return user.getUsername();
        }
        return user.getEmail();
    }

    private String stringAttribute(OAuth2User oAuth2User, String key) {
        Object value = oAuth2User.getAttribute(key);
        return value == null ? "" : value.toString();
    }
}
