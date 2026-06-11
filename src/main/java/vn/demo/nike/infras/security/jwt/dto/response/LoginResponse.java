package vn.demo.nike.infras.security.jwt.dto.response;

public record LoginResponse (
        String accessToken,
        String tokenType,
        long expiresInSeconds,
        Long userId,
        String email
) {

}
