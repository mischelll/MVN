package demoprojects.demo.web.controllers;

import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.service.EmailService;
import demoprojects.demo.service.UserService;
import demoprojects.demo.service.models.UserRegisterServiceModel;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.Random;

import static demoprojects.demo.util.Messages.SUCCESSFUL_REG;

@Controller
@RequestMapping("/mvn/users")
public class UserAuthController extends BaseController {
    private final UserService userService;
    private final ModelMapper mapper;
    private final EmailService emailService;
    private int path;
    private String email;



    @Autowired
    public UserAuthController(UserService userService, ModelMapper mapper, EmailService emailService) {
        this.userService = userService;
        this.mapper = mapper;
        this.emailService = emailService;
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

        if (this.userService.register(this.mapper.map(user, UserRegisterServiceModel.class)) == null) {
            return new ModelAndView("auth/register");
        }
        path = getRandomPath();
        email = user.getEmail();
        emailService.sendEmail(user.getEmail(), "Successful Registration", String.format(Messages.getSuccessfulReg(), user.getUsername(), path));
        return new ModelAndView("auth/email-verification");
    }

    @GetMapping("/registration/{s}")
    public ModelAndView emailConfirmRegistration(@PathVariable("s") int code) {
        if (path != code) {
            path = getRandomPath();
            emailService.sendEmail(email, "Successful Registration", String.format(Messages.resendVerification(), path));
            return super.redirect("/mvn/users/login");
        }
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

