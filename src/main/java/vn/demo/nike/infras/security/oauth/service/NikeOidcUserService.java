package vn.demo.nike.infras.security.oauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.user.entity.User;
import vn.demo.nike.infras.security.oauth.entity.NikeOidcUser;

@Service
public class NikeOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final OAuth2UserProvisioningService oAuth2UserProvisioningService;
    private final OidcUserService oidcUserService;

    @Autowired
    public NikeOidcUserService(OAuth2UserProvisioningService oAuth2UserProvisioningService) {
        this(oAuth2UserProvisioningService, new OidcUserService());
    }

    NikeOidcUserService(
            OAuth2UserProvisioningService oAuth2UserProvisioningService,
            OidcUserService oidcUserService
    ) {
        this.oAuth2UserProvisioningService = oAuth2UserProvisioningService;
        this.oidcUserService = oidcUserService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = oidcUserService.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String subject = stringClaim(oidcUser, "sub");
        String email = stringClaim(oidcUser, "email");
        boolean emailVerified = Boolean.TRUE.equals(oidcUser.getClaims().get("email_verified"));
        String name = stringClaim(oidcUser, "name");

        User user = oAuth2UserProvisioningService.provision(
                provider,
                subject,
                email,
                emailVerified,
                name
        );

        return new NikeOidcUser(
                user.getId(),
                user.getEmail(),
                resolveDisplayName(user, name),
                user.getAuthorities(),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo()
        );
    }

    private String resolveDisplayName(User user, String oidcDisplayName) {
        if (oidcDisplayName != null && !oidcDisplayName.isBlank()) {
            return oidcDisplayName;
        }
        if (user.getUsername() != null && !user.getUsername().isBlank()) {
            return user.getUsername();
        }
        return user.getEmail();
    }

    private String stringClaim(OidcUser oidcUser, String key) {
        Object value = oidcUser.getClaims().get(key);
        return value == null ? "" : value.toString();
    }
}
