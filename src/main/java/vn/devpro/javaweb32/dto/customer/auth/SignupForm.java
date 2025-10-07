package vn.devpro.javaweb32.dto.customer.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.AssertTrue;
import java.io.Serializable;

public class SignupForm implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Tên người dùng không được để trống")
    @Size(min = 3, max = 50, message = "Tên người dùng phải từ 3 đến 50 ký tự")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    // Mật khẩu mạnh: ít nhất 8 ký tự, ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số
    @NotBlank(message = "Mật khẩu không được để trống")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Mật khẩu phải ≥8 ký tự, gồm chữ hoa, chữ thường và số"
    )
    private String password;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;

    public SignupForm() {}

    public SignupForm(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Getters / Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    // Kiểm tra confirm password khớp (được gọi bởi Validator khi dùng @Valid)
    @AssertTrue(message = "Mật khẩu xác nhận không khớp")
    public boolean isPasswordsMatching() {
        if (password == null) return confirmPassword == null;
        return password.equals(confirmPassword);
    }
}
