package demoprojects.demo.web.controllers;

import demoprojects.demo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
@Controller
@RequestMapping("/mvn/users/api")
public class UserController extends BaseController{
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/delete/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteByUsername(@PathVariable("username") String username){
        if (!this.userService.deleteByUsername(username)){
            super.redirect("/logout");
        }

        return new ModelAndView("home/home");
    }

}
