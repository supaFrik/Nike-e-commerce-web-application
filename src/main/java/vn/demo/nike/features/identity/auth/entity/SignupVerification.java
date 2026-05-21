package vn.demo.nike.features.identity.auth.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import vn.demo.nike.shared.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SignupVerification extends BaseEntity {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String codeHash;

    @NotNull
    private Integer attemptCount;

    private LocalDateTime expiresAt;

    private LocalDateTime lastSentAt;

    private LocalDateTime usedAt;
}