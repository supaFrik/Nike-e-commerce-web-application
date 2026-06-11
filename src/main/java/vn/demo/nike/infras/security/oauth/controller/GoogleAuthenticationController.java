package vn.demo.nike.infras.security.oauth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import vn.demo.nike.infras.security.jwt.dto.response.LoginResponse;
import vn.demo.nike.infras.security.oauth.dto.request.GoogleTokenLoginRequest;

@RestController
@RequiredArgsConstructor
public class GoogleAuthenticationController {
    @PostMapping("/api/v1/auth/oauth2/google")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody GoogleTokenLoginRequest request) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Google mobile token exchange is not implemented yet");
    }
}
