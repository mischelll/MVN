package demoprojects.demo.web.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileEditModel {
    private String bio;
    private String fullName;
    private String email;
}
