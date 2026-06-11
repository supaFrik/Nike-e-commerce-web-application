package vn.demo.nike.features.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.auth.entity.SignupVerification;
import vn.demo.nike.features.auth.repository.SignUpVerificationRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignUpVerificationService {
    private static final int CODE_LENGTH = 8;
    private static final int EXPIRED_MINUTES = 15;
    private static final int RESEND_COOLDOWN_SECONDS = 30;
    private static final int MAX_ATTEMPTS = 5;

    private final SignUpVerificationRepository signUpVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final SecureRandom secureRandom = new SecureRandom();

    private String generateCode(int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(secureRandom.nextInt(10));
        }
        return sb.toString();
    }

    @Transactional
    public void sendCode(String email) {
        LocalDateTime now = LocalDateTime.now();
        String normalizedEmail = email.trim().toLowerCase();

        signUpVerificationRepository.findTopByEmailOrderByCreateDateDesc(normalizedEmail)
                .ifPresent(signUpVerification -> {
                    LocalDateTime lastSentAt = signUpVerification.getLastSentAt();
                    if (lastSentAt != null && lastSentAt.plusSeconds(RESEND_COOLDOWN_SECONDS).isAfter(now)) {
                        throw new IllegalStateException("Please wait before requesting a new code");
                    }
                });

        String rawCode = generateCode(CODE_LENGTH);
        String hashCode = passwordEncoder.encode(rawCode);

        SignupVerification signUpVerification = SignupVerification.builder()
                .email(normalizedEmail)
                .codeHash(hashCode)
                .attemptCount(0)
                .expiresAt(now.plusMinutes(EXPIRED_MINUTES))
                .lastSentAt(now)
                .usedAt(null)
                .build();
        signUpVerificationRepository.save(signUpVerification);
        mailService.sendVerificationCode(normalizedEmail, rawCode);
    }

    @Transactional
    public boolean verifyCode(String email, String code) {
        if (email == null || code == null) {
            return false;
        }

        String normalizedEmail = email.trim().toLowerCase();
        SignupVerification signupVerification = signUpVerificationRepository.findTopByEmailOrderByCreateDateDesc(normalizedEmail)
                .orElse(null);

        if (signupVerification == null
                || signupVerification.getUsedAt() != null
                || signupVerification.getExpiresAt().isBefore(LocalDateTime.now())
                || signupVerification.getAttemptCount() >= MAX_ATTEMPTS) {
            return false;
        }

        boolean matchedCode = passwordEncoder.matches(code, signupVerification.getCodeHash());
        if (!matchedCode) {
            signupVerification.setAttemptCount(signupVerification.getAttemptCount() + 1);
            signUpVerificationRepository.save(signupVerification);
            return false;
        }

        signupVerification.setUsedAt(LocalDateTime.now());
        signUpVerificationRepository.save(signupVerification);

        return true;
    }
}
