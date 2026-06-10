package vn.demo.nike.infras.security.oauth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class OAuth2User implements org.springframework.security.oauth2.core.user.OAuth2User {
    private final Long id;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        Object value = attributes.get(nameAttributeKey);
        return value == null ? null : value.toString();
    }
}
