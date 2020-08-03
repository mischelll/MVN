package demoprojects.demo.web.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestModel {

    @Email(message = "Please enter a valid email")
    @NotEmpty(message = "Field cannot be empty")
    private String email;

    @NotEmpty(message = "Message must have some text!")
    @NotBlank(message = "Text cannot be empty!")
    @Size(min = 10, message = "Text must be more than 10 characters! ")
    private String text;
}
