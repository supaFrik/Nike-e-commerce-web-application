package vn.demo.nike.infras.security.oauth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GoogleTokenLoginRequest (
        @NotBlank String idToken
) {
}
