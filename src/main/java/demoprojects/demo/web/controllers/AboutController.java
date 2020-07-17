package demoprojects.demo.web.controllers;

import demoprojects.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class AboutController {
    private final UserService userService;

    public AboutController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/about")
    public ModelAndView getAbout(ModelAndView modelAndView, Principal principal){
        modelAndView.addObject("user",
                this.userService.findByUsername(principal.getName()));
        modelAndView.setViewName("about/about");
        return modelAndView;
    }

    @GetMapping("/about/goal")
    public ModelAndView getGoal(){
        return new ModelAndView("about/goal");
    }


}
