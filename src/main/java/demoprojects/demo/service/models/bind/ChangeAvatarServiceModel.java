package demoprojects.demo.service.models.bind;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeAvatarServiceModel {
    private String imgUrl;
    private String format;
    private String name;
}
