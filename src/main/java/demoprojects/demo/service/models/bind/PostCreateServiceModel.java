package demoprojects.demo.service.models.bind;

import demoprojects.demo.dao.models.entities.CategoryName;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostCreateServiceModel {
    @NotEmpty(message = "Post must have a title!")
    @NotBlank(message = "Title cannot be empty!")
    @Size(min = 2, message = "Title must be minimum 3 characters!")
    private String title;

    @NotEmpty(message = "Post must have a preview!")
    @NotBlank(message = "Preview cannot be empty!")
    @Size(min = 2, message = "Preview must be minimum 3 characters!")
    private String preview;

    @NotEmpty(message = "Post must have some content!")
    @NotBlank(message = "Content cannot be empty!")
    @Size(min = 100, message = "Content must be more than 100 characters ")
    private String content;

    @NotNull(message = "Please select a category!")
    private List<CategoryName> category;

    @NotBlank(message = "URL cannot be empty!")
    private String imgUrl;

    @NonNull
    private String author;
}
