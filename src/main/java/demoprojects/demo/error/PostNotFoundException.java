package demoprojects.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@ResponseStatus(code = NOT_FOUND,reason = "Post not found!")
public class PostNotFoundException extends BaseException {
    public PostNotFoundException() {
        super(404);
    }

    public PostNotFoundException(String message) {
        super(404, message);
    }
}
