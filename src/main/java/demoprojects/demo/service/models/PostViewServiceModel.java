package demoprojects.demo.service.models;

import demoprojects.demo.dao.models.entities.CategoryName;
import demoprojects.demo.dao.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostViewServiceModel {
    private String title;
    private String content;
    private String author;
    private String imgUrl;
    private CategoryName categories;
    private Integer commentsCount;
    private LocalDateTime postedOn;
}
