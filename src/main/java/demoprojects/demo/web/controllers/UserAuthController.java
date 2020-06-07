package demoprojects.demo.web.controllers;

import demoprojects.demo.service.UserService;
import demoprojects.demo.service.models.UserRegisterServiceModel;
import demoprojects.demo.web.models.UserRegisterModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/mvn/users")
public class UserAuthController extends BaseController {
    private final UserService userService;
    private final ModelMapper mapper;

    @Autowired
    public UserAuthController(UserService userService, ModelMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("/login")
    public ModelAndView getLogin(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            /* The user is logged in :) */
            return new ModelAndView("redirect:/home");
        }

        return new ModelAndView("auth/login");
    }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        return new ModelAndView("auth/register2");
    }

    @PostMapping("/register")
    public ModelAndView getRegisterConfirm(@Valid @ModelAttribute("user") UserRegisterModel user, BindingResult bindingResult, Model model) {
        model.addAttribute(user);
        if (!this.userService.register(this.mapper.map(user, UserRegisterServiceModel.class))) {
            return new ModelAndView("auth/register");

        }
        return new ModelAndView("auth/login");
    }
}
