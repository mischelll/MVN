package demoprojects.demo.service.models.bind;

import demoprojects.demo.dao.models.entities.FileFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductImageCreateServiceModel {
    private String imgUrl;
    private String format;
    private String name;

}
