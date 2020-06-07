package demoprojects.demo.service.models;

import demoprojects.demo.dao.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostViewServiceModel {
    private String title;
    private String content;
    private String author;
    private LocalDateTime postedOn;
}
