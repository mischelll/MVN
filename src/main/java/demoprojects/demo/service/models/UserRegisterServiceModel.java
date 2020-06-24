package demoprojects.demo.service.models;

import demoprojects.demo.annottation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterServiceModel  {
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
}
