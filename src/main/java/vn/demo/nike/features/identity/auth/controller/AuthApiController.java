package vn.demo.nike.features.identity.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.demo.nike.features.identity.auth.dto.AvailabilityResponse;
import vn.demo.nike.features.identity.auth.dto.MessageResponse;
import vn.demo.nike.features.identity.auth.request.SignUpVerificationRequest;
import vn.demo.nike.features.identity.auth.service.SignUpVerificationService;
import vn.demo.nike.features.identity.user.repository.UserRepository;

import java.util.Map;

@RestController
@RequiredArgsConstructor

public class AuthApiController {

    private final UserRepository userRepository;
    private final SignUpVerificationService signUpVerificationService;

    @PostMapping("/api/v1/auth/verification-codes")
    @ResponseBody
    public ResponseEntity<MessageResponse> sendVerificationCode(
            @Valid @RequestBody SignUpVerificationRequest request
    ) {
        String email = request.email().trim().toLowerCase();
        if (!userRepository.existsByEmail(email)) {
            signUpVerificationService.sendCode(email);
        }
        return ResponseEntity.ok(new MessageResponse("Code xác nhận đã được gửi, Vui lòng kiểm tra hộp thư email !"));
    }

    @GetMapping("/api/v1/auth/availability")
    @ResponseBody
    public ResponseEntity<AvailabilityResponse> checkAvailability(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {
        boolean usernameExists = username != null
                && !username.trim().isEmpty()
                && userRepository.existsByUsername(username.trim());
        boolean emailExists = email != null
                && !email.trim().isEmpty()
                && userRepository.existsByEmail(email.trim().toLowerCase());

        return ResponseEntity.ok(new AvailabilityResponse(usernameExists, emailExists));
    }
}
