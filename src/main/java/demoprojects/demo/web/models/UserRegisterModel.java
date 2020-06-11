package demoprojects.demo.web.models;

import demoprojects.demo.annottation.ValidPassword;


import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;



public class UserRegisterModel {
    @NotEmpty
    @Min(value = 3,message = "Username must be at least 3 chars long")
    @Max(value = 16, message = "Username cannot be longer than 16 chars")
    private String username;
    @Email(message = "Please enter a valid email")
    @NotEmpty
    private String email;
   @NotEmpty
   @ValidPassword(message = "Invalid password")
    private String password;
    private String confirmPassword;

    public UserRegisterModel() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
