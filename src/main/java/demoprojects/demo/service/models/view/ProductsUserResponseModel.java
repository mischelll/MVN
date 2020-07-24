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
public class ProductsUserResponseModel {
    private String id;
    private String title;
    private BigDecimal price;
    private String createdOn;
    private Integer views;
    private Boolean isSold;
}
