package vn.demo.nike.features.auth.request;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;
    private String password;
    private String email;
}
