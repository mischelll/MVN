package demoprojects.demo.web.models;


import demoprojects.demo.dao.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostCreateModel {
    private String title;
    private String content;
    private String author;
}
