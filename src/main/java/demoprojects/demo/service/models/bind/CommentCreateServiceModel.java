package demoprojects.demo.service.models.bind;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateServiceModel {
    private String author;
    private String postID;
    private String text;
    private LocalDateTime date;

}
