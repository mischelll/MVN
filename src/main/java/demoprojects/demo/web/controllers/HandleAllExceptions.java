package demoprojects.demo.web.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class HandleAllExceptions {
    @ExceptionHandler(Throwable.class)
    public ModelAndView handleException(Throwable ex) {
        ModelAndView model = new ModelAndView("error");
        ex = new Throwable("Wrong URL!");
        model.addObject(ex.getMessage());

        return model;
    }
}
