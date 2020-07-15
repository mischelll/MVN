package demoprojects.demo.service.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentViewServiceModel {
    private String author;
    private String date;
    private String text;
    private Integer likes;
    private Integer dislikes;
}
