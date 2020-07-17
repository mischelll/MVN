package demoprojects.demo.web.controllers;

import demoprojects.demo.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class HomeController extends BaseController {
    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/")
    public ModelAndView getIndex() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            /* The user is logged in :) */
            return new ModelAndView("redirect:/home");
        }
        return new ModelAndView("index1");
    }

    @GetMapping("/home")
    public ModelAndView getHome(ModelAndView modelAndView, Principal principal) {
        modelAndView.addObject("user",
                this.userService.findByUsername(principal.getName()));
        modelAndView.setViewName("home/home");
        return modelAndView;
    }


}
