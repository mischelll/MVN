package demoprojects.demo.service.models.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseModel {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String roles;
    private String registeredOn;
    private String gender;
    private Integer postsCount;
}
