package vn.demo.nike.infras.security.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import vn.demo.nike.infras.security.oauth.entity.OAuth2User;
import vn.demo.nike.features.user.entity.User;


@RequiredArgsConstructor
public class NikeOAuth2UserService implements org.springframework.security.oauth2.client.userinfo.OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final OAuth2UserProvisioningService  oAuth2UserProvisioningService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String subject = stringAttribute(oAuth2User, "sub");
        String email = stringAttribute(oAuth2User, "email");
        boolean emailVerified = Boolean.TRUE.equals(oAuth2User.getAttributes().get("email_verified"));
        String name = stringAttribute(oAuth2User, "name");

        User user = oAuth2UserProvisioningService.provision(
                provider,
                subject,
                email,
                emailVerified,
                name
        );

        return new OAuth2User(
                user.getId(),
                user.getEmail(),
                user.getAuthorities(),
                oAuth2User.getAttributes(),
                "sub"
        );
    }

    private String stringAttribute(OAuth2User oAuth2User, String key) {
        Object value = oAuth2User.getAttribute(key);
        return value == null ? "" : value.toString();
    }
}
