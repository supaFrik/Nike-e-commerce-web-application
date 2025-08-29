package vn.devpro.javaweb32.dto.customer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignupForm {
    @NotBlank(message = "Cannot leave username blank")
    @Size(min = 2, max = 50)
    private String username;

    @NotBlank(message = "Cannot leave email blank")
    @Email(message = "Email is invalid")
    private String email;

    public SignupForm() {
    }

    public SignupForm(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
