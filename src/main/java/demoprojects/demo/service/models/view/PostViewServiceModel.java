package demoprojects.demo.service.models.view;

import demoprojects.demo.dao.models.entities.PostCategoryName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostViewServiceModel {
    private String id;
    private String title;
    private String preview;
    private String content;
    private String author;
    private String imgUrl;
    private PostCategoryName categories;
    private Integer commentsCount;
    private String postedOn;
}
