package demoprojects.demo.web.controllers;

import demoprojects.demo.service.interfaces.user.RoleService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.web.models.ProfileEditModel;
import demoprojects.demo.web.models.RoleChangeModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/mvn/users/api")
public class UserController extends BaseController {
    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/insert")
    public ModelAndView insert() {
        this.roleService.addNewRole();
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/delete/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteByUsername(@PathVariable("username") String username) {
        if (!this.userService.deleteByUsername(username)) {
            super.redirect("/logout");//change it
        }

        return new ModelAndView("home/home");
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(@RequestParam String id, ModelAndView modelAndView) {
        modelAndView.addObject("user", this.userService.getUserProfile(id));
        modelAndView.setViewName("user/profile");

        return modelAndView;
    }


    @GetMapping("/profile-view/{username}")
    public ModelAndView getProfileView(@PathVariable String username, ModelAndView modelAndView) {
        modelAndView.addObject("user", this.userService.getUserVIewProfile(username));
        modelAndView.setViewName("user/profile-view");

        return modelAndView;
    }

    @GetMapping("/profile/edit")
    public ModelAndView getEditProfile(@RequestParam String id, ModelAndView modelAndView) {
        modelAndView.addObject("user", this.userService.getUserProfile(id));
        modelAndView.addObject("user2",new ProfileEditModel());
        modelAndView.setViewName("user/profile-edit");

        return modelAndView;
    }
    @PostMapping("/profile/edit")
    public ModelAndView getEditProfileConfirm(@Valid @ModelAttribute("user2") ProfileEditModel user2,
                                              BindingResult bindingResult,
                                              @RequestParam String id,
                                              ModelAndView modelAndView){


        modelAndView.setViewName("redirect:/mvn/users/api/profile?id=" + id);

        return modelAndView;
    }



}
