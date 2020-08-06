package demoprojects.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Product not found!")
public class ProductNotFoundException extends BaseException{
    public ProductNotFoundException() {
        super(404);
    }

    public ProductNotFoundException(String message) {
        super(404, message);
    }
}
