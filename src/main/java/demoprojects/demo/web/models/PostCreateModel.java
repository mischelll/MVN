package demoprojects.demo.web.models;


import demoprojects.demo.dao.models.entities.User;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostCreateModel {
    @NotEmpty
    @NonNull
    @Size(min = 2, max = 54, message = "Title too long or too short")
    private String title;
    @NotEmpty
    @NonNull
    @Size(min = 2, message = "Content must be more than 2 characters ")
    private String content;
    private String author;
    @NotEmpty
    @NonNull
    private String category;
}
