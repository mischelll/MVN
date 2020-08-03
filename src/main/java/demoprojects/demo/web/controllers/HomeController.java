package demoprojects.demo.web.controllers;

import demoprojects.demo.service.interfaces.user.EmailService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.util.messaging.SmsSenderAndReceiver;
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
    private final SmsSenderAndReceiver smsSenderAndReceiver;


    public HomeController(UserService userService, SmsSenderAndReceiver smsSenderAndReceiver, EmailService emailService) {
        this.userService = userService;
        this.smsSenderAndReceiver = smsSenderAndReceiver;

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
    public ModelAndView getHome(ModelAndView modelAndView, Principal principal) throws Exception {
        modelAndView.addObject("user",
                this.userService.findByUsername(principal.getName()));
        modelAndView.setViewName("home/home");



        return modelAndView;
    }


}
