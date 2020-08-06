package demoprojects.demo.service.models.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductNewServiceModel {
    private String id;
    private String title;
    private String fullName;
    private String username;
    private String created;
    private BigDecimal price;
    private String imgURL;
    private Integer views;
    private String email;
    private String contactNumber;
    private String categories;

}
