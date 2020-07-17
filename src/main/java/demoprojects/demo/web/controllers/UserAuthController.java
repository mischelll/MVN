package demoprojects.demo.web.controllers;

import demoprojects.demo.service.EmailService;
import demoprojects.demo.service.UserService;
import demoprojects.demo.service.models.bind.UserRegisterServiceModel;
import demoprojects.demo.util.Messages;
import demoprojects.demo.web.models.UserRegisterModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.Random;

@Controller
@RequestMapping("/mvn/users")
public class UserAuthController extends BaseController {
    private final UserService userService;
    private final ModelMapper mapper;
    private final EmailService emailService;
    private int path;
    private String email;
    private String username;


    @Autowired
    public UserAuthController(UserService userService, ModelMapper mapper, EmailService emailService) {
        this.userService = userService;
        this.mapper = mapper;
        this.emailService = emailService;
    }

    @GetMapping("/login")
    public ModelAndView getLogin(HttpSession session,Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            /* The user is logged in :) */
            return new ModelAndView("redirect:/home");
        }

        return new ModelAndView("auth/login");
    }

    @GetMapping("/register")
    public ModelAndView getRegister(Model model, ModelAndView modelAndView) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            /* The user is logged in :) */
            return new ModelAndView("redirect:/home");
        }
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserRegisterModel());
        }
        modelAndView.setViewName("auth/register2");
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView getRegisterConfirm(@Valid @ModelAttribute("user") UserRegisterModel user,
                                           BindingResult bindingResult,
                                           RedirectAttributes redirectAttributes,
                                           ModelAndView modelAndView
                                           ) {
        if (bindingResult.hasErrors() || !user.getPassword().equals(user.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            modelAndView.setViewName("redirect:/mvn/users/register");
        } else {
            UserRegisterServiceModel serviceModel = this.mapper.map(user, UserRegisterServiceModel.class);
            this.userService.register(serviceModel);

            modelAndView.setViewName("auth/email-verification");
            path = getRandomPath();
            email = user.getEmail();
            emailService.sendEmail(user.getEmail(), "Successful Registration",
                    String.format(Messages.getSuccessfulReg(), user.getUsername(), path,user.getUsername()));
        }

        return modelAndView;
    }

    @GetMapping("/registration/{code}/{username}")
    public ModelAndView emailConfirmRegistration(@PathVariable("code") int code,@PathVariable("username") String username ) {

        if (path != code) {
            path = getRandomPath();
            emailService.sendEmail(email, "Successful Registration", String.format(Messages.resendVerification(), path));
            return super.redirect("/mvn/users/login");
        }
        this.userService.activateAccount(username);
        this.username = null;
        email = null;
        path = 0;

        return super.redirect("/mvn/users/login");
    }

    private int getRandomPath() {
        Random random = new Random();
        int low = 1000;
        int high = 10000;

        return random.nextInt(high - low) + low;//generate random url number

    }
}

