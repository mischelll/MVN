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
public class ProductsUserResponseModel {
    private String id;
    private String title;
    private BigDecimal price;
    private String createdOnDate;
    private String buyerUsername;
    private String sellerUsername;
    private String soldOnDate;
    private String boughtOnDate;
    private Integer views;
    private Boolean isSold;
    private BigDecimal revenue;
}
