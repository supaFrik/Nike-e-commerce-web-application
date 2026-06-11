package vn.demo.nike.infras.security.oauth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;

@Getter
@AllArgsConstructor
public class NikeOidcUser implements OidcUser {
    private final Long id;
    private final String email;
    private final String displayName;
    private final Collection<? extends GrantedAuthority> authorities;
    private final OidcIdToken idToken;
    private final OidcUserInfo userInfo;

    @Override
    public Map<String, Object> getClaims() {
        return userInfo == null ? idToken.getClaims() : userInfo.getClaims();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return getClaims();
    }

    @Override
    public String getName() {
        if (displayName != null && !displayName.isBlank()) {
            return displayName;
        }
        return email;
    }
}
