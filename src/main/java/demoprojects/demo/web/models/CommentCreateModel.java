package demoprojects.demo.web.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateModel {
    private String author;
    private String postID;
    @NotEmpty(message = "Comment must have some content!")
    @NotBlank(message = "Comment cannot be empty!")
    @Size(min = 2, message = "Comment must be more than 1 character. ")
    private String text;
    private LocalDateTime date;
}
