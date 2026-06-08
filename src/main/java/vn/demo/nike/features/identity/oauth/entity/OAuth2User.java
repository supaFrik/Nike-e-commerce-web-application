package vn.demo.nike.features.identity.oauth.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class OAuth2User implements org.springframework.security.oauth2.core.user.OAuth2User {
    private final Long id;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;

    public OAuth2User(Long id, String email, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey) {
        this.id = id;
        this.email = email;
        this.authorities = authorities;
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
    }

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
