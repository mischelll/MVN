package demoprojects.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User not found")
public class UserNotFoundException  extends BaseException{
    public UserNotFoundException() {
        super(404);
    }

    public UserNotFoundException( String message) {
        super(404, message);
    }
}
