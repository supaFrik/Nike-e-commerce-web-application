package vn.demo.nike.infras.security.jwt.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.security.jwt")
public record JwtProperties (
        String issuer,
        Duration accessTokenTtl,
        String secret
) {
}
