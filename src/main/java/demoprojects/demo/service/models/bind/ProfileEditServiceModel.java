package demoprojects.demo.service.models.bind;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEditServiceModel {
    private String id;
    private String registeredOn;
    private String gender;
    private String bio;
    private String username;
    @NotEmpty(message = "Field cannot be empty")
    @Size(min = 2, message = "FullName must be minimum 2 characters")
    private String fullName;

    @Email(message = "Please enter a valid email")
    @NotEmpty(message = "Field cannot be empty")
    private String email;
}
