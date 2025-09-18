package vn.devpro.javaweb32.dto.customer.auth;

public class SignupForm {
    private String username;
    private String email;

    public SignupForm() {
    }

    public SignupForm(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // getter & setter
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
