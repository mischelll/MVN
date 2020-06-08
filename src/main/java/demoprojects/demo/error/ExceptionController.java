package demoprojects.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Level;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Page not found")
public class ExceptionController  extends RuntimeException{
    public ExceptionController(String message) {
        super(message);
    }
}
