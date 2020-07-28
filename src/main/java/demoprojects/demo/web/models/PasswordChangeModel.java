package demoprojects.demo.web.models;

import demoprojects.demo.annottation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeModel {
    private String id;
    @NotEmpty(message = "Field cannot be empty")
    private String oldPassword;
    @NotEmpty(message = "Field cannot be empty")
    @ValidPassword(message = "Invalid password!")
    private String newPassword;
    @NotEmpty(message = "Field cannot be empty")
    @ValidPassword(message = "Invalid password!")
    private String confirmNewPassword;
}
