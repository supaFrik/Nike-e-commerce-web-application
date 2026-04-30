package vn.demo.nike.features.identity.auth.request;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;
    private String password;
    private String email;
}
