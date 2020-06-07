package demoprojects.demo.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AboutController {
    @GetMapping("/about")
    public ModelAndView getAbout(){
        return new ModelAndView("about/about");
    }

    @GetMapping("/about/goal")
    public ModelAndView getGoal(){
        return new ModelAndView("about/goal");
    }


}
