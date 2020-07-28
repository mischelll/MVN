package demoprojects.demo.web.controllers;

import demoprojects.demo.service.interfaces.user.EmailService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.service.models.bind.UserRegisterServiceModel;
import demoprojects.demo.util.Messages;
import demoprojects.demo.web.models.ResetPasswordModel;
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
    public ModelAndView getLogin(HttpSession session, Model model) {
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
        boolean matching = user.getPassword().equals(user.getConfirmPassword());
        if (bindingResult.hasErrors() || !matching) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("matching", !matching);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            modelAndView.setViewName("redirect:/mvn/users/register");
        } else {
            UserRegisterServiceModel serviceModel = this.mapper.map(user, UserRegisterServiceModel.class);
            boolean emailAvailable = this.userService.isEmailAvailable(serviceModel.getEmail());
            boolean usernameAvailable = this.userService.isUsernameAvailable(serviceModel.getUsername());

            if (!usernameAvailable) {
                redirectAttributes.addFlashAttribute("user", user);
                redirectAttributes.addFlashAttribute("foundUsername", true);
                modelAndView.setViewName("redirect:/mvn/users/register");
            } else if (!emailAvailable) {
                redirectAttributes.addFlashAttribute("user", user);
                redirectAttributes.addFlashAttribute("foundEmail", true);
                modelAndView.setViewName("redirect:/mvn/users/register");
            } else {
                UserRegisterServiceModel register = this.userService.register(serviceModel);
                modelAndView.setViewName("redirect:/mvn/users/auth/email-verification");
                path = getRandomPath();
                email = user.getEmail();
                emailService.sendEmail(user.getEmail(), "Successful Registration",
                        String.format(Messages.getSuccessfulReg(), user.getUsername(), path, user.getUsername()));
            }
        }

        return modelAndView;
    }

    @GetMapping("/registration/{code}/{username}")
    public ModelAndView emailConfirmRegistration(@PathVariable("code") int code, @PathVariable("username") String username) {

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

    @GetMapping("/auth/email-verification")
    public ModelAndView emailVerification(ModelAndView modelAndView) {
        modelAndView.setViewName("auth/email-verification");

        return modelAndView;
    }

    @GetMapping("/forgot-password")
    public ModelAndView getResetPassword(ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("resetPassword")) {
            model.addAttribute("resetPassword", new ResetPasswordModel());
        }
        modelAndView.setViewName("auth/forgot-password");
        return modelAndView;
    }

    @PostMapping("/forgot-password")
    public ModelAndView getResetPasswordConfirm(@Valid @ModelAttribute("resetPassword")
                                                        ResetPasswordModel resetPassword,
                                                BindingResult bindingResult,
                                                RedirectAttributes redirectAttributes,
                                                ModelAndView modelAndView) {
        boolean emailAvailable = this.userService.isEmailAvailable(resetPassword.getEmail());
        if (bindingResult.hasErrors() || emailAvailable){
            redirectAttributes.addFlashAttribute("resetPassword",resetPassword);
            redirectAttributes.addFlashAttribute("invalid",emailAvailable);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.resetPassword", bindingResult);
            modelAndView.setViewName("redirect:/mvn/users/forgot-password");
        }else {

            String resetPasswprd = this.userService.resetPassword(resetPassword.getEmail());
            this.emailService.sendEmail(resetPassword.getEmail(),"Password Reset",
                    String.format("Dear user,%nThis is your new password: %s %n" +
                            "!!PLEASE CHANGE THIS TEMPORARY PASSWORD AFTER LOGGING IN AND DELETE THIS EMAIL!!%n%n" +
                            "The MVN Team. All rights reserved.",resetPasswprd));
            modelAndView.setViewName("redirect:/mvn/users/successful-reset");
        }

        return modelAndView;
    }

    @GetMapping("/successful-reset")
    public ModelAndView successfulReset(ModelAndView modelAndView){
        modelAndView.setViewName("auth/successful-reset");

        return modelAndView;
    }
}

