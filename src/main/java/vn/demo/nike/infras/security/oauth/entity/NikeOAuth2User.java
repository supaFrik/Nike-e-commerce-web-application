package vn.demo.nike.infras.security.oauth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

@Getter
@AllArgsConstructor
public class NikeOAuth2User implements org.springframework.security.oauth2.core.user.OAuth2User {
    private final Long id;
    private final String email;
    private final String displayName;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        if (displayName != null && !displayName.isBlank()) {
            return displayName;
        }
        return email;
    }
}
