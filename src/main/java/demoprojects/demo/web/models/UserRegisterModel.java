package demoprojects.demo.web.models;

import demoprojects.demo.annottation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.Valid;
import javax.validation.constraints.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegisterModel {
    @NotEmpty(message = "Field cannot be empty")
    @Size(min = 3, max = 32, message = "Username must be between 3 and 32 characters")
    private String username;

    @NotEmpty(message = "Field cannot be empty")
    @Size(min = 2, message = "First name must be minimum 2 characters")
    private String firstName;

    @NotEmpty(message = "Field cannot be empty")
    @Size(min = 2, message = "Last name must be minimum 2 characters")
    private String lastName;

    @NotEmpty(message = "Field cannot be empty")
    private String gender;

    @Email(message = "Please enter a valid email")
    @NotEmpty(message = "Field cannot be empty")
    private String email;

    @NotEmpty(message = "Field cannot be empty")
    @ValidPassword(message = "Invalid password!")
    private String password;

    @NotEmpty(message = "Field cannot be empty")
    @ValidPassword(message = "Invalid password!")
    private String confirmPassword;

}
