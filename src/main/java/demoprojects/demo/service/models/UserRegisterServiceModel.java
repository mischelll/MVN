package demoprojects.demo.service.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterServiceModel  {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
