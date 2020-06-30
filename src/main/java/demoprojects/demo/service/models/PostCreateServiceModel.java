package demoprojects.demo.service.models;

import demoprojects.demo.dao.models.entities.CategoryName;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostCreateServiceModel {
    @NotEmpty(message = "Post must have a title!")
    @NotBlank(message = "Title cannot be empty!")
    @Size(min = 2,  message = "Title must be minimum 3 characters!")
    private String title;
    @NotEmpty(message = "Post must have some content!")
    @NotBlank(message = "Content cannot be empty!")
    @Size(min = 10, message = "Content must be more than 10 characters ")
    private String content;
    @NotNull(message = "Please select a category!")
    private CategoryName category;
    @NotBlank(message = "URL cannot be empty!")
    private String imgUrl;
    @NonNull
    private String author;
}
