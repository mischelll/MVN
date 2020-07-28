package demoprojects.demo.web.models;

import demoprojects.demo.dao.models.entities.ProductCategoryName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEditModel {
    private String id;

    @NotEmpty(message = "Product must have a title!")
    @NotBlank(message = "Title cannot be empty!")
    @Size(min = 2, message = "Title must be minimum 3 characters!")
    private String title;

    @NotEmpty(message = "Product must have a preview!")
    @NotBlank(message = "Preview cannot be empty!")
    @Size(min = 2, message = "Preview must be minimum 3 characters!")
    private String preview;

    @NotEmpty(message = "Product must have some content!")
    @NotBlank(message = "Description cannot be empty!")
    @Size(min = 100, message = "Content must be more than 100 characters ")
    private String description;

    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price must be a positive number")
    private BigDecimal price;

    @NotEmpty(message = "Tel. number cannot be empty")
    @NotBlank(message = "Tel. number cannot be empty")
    @Size(min = 13,max = 13, message = "Number cannot be more or less than 13 characters.")
    @Pattern(regexp = "^\\+359[0-9 ]{9}$")
    private String telephoneNumber;

    @NotNull(message = "Please select a category!")
    private List<ProductCategoryName> category;
}
