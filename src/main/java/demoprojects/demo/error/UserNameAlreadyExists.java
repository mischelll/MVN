package demoprojects.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.IM_USED, reason = "Username already exists")
public class UserNameAlreadyExists extends BaseException {

    public UserNameAlreadyExists() {
        super(226);
    }

    public UserNameAlreadyExists( String message) {
        super(226, message);
    }
}
