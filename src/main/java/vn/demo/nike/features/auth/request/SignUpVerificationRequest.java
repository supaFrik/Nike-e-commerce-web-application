package vn.demo.nike.features.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpVerificationRequest(
        @Email
        @NotBlank
        String email
) {
}
