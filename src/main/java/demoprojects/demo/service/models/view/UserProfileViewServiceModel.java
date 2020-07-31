package demoprojects.demo.service.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileViewServiceModel {
    private String id;
    private String imgUrl;
    private String username;
    private String gender;
    private String email;
    private String fullName;
    private String bio;
    private String registeredOn;
    private Integer postsSize;
    private Integer postsComments;
    private Integer productsSize;
    private Integer productsSoldSize;
    private Integer gallerySize;
    private Integer galleryComments;

}
