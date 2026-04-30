package vn.demo.nike.features.identity.user.request;

import lombok.Getter;

@Getter
public class UserCreateRequest {
    private String email;
    private String password;
}
