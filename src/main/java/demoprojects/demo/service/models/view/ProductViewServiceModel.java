package demoprojects.demo.service.models.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductViewServiceModel {
    private String id;
    private String title;
    private Integer views;
    private String description;
    private String fullName;
    private String preview;
    private String created;
    private String email;
    private String categories;
    private BigDecimal price;
    private String username;
    private String telephone;
    private List<String> imgUrls;
}
