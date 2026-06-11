package vn.demo.nike.infras.security.jwt.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.demo.nike.features.auth.request.LoginRequest;
import vn.demo.nike.features.user.entity.User;
import vn.demo.nike.infras.security.jwt.dto.response.LoginResponse;
import vn.demo.nike.infras.security.jwt.properties.JwtProperties;
import vn.demo.nike.infras.security.jwt.service.JwtService;

@RequiredArgsConstructor
@RestController
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        String token = jwtService.createAccessToken(user);
        return ResponseEntity.ok(new LoginResponse(
                token,
                "Bearer",
                jwtProperties.accessTokenTtl().toSeconds(),
                user.getId(),
                user.getEmail()
        ));
    }
}
