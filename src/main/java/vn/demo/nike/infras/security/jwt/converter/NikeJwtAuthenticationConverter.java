package vn.demo.nike.infras.security.jwt.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NikeJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Nullable
    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        List<String> roles = source.getClaimAsStringList("roles");
        var authorities = roles == null ? List.<SimpleGrantedAuthority>of() : roles.stream()
                                                                              .map(SimpleGrantedAuthority::new)
                                                                              .toList();
        return new JwtAuthenticationToken(source, authorities, source.getSubject());
    }
}
