package demoprojects.demo.service.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPopularViewModel {
    private String id;
    private String postedOn;
    private String title;
    private String author;
    private Integer commentsCount;
    private String imgUrl;
}
