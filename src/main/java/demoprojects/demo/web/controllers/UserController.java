package demoprojects.demo.web.controllers;

import demoprojects.demo.service.interfaces.CloudinaryService;
import demoprojects.demo.service.interfaces.shop.ImageService;
import demoprojects.demo.service.interfaces.user.RoleService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.service.models.bind.ChangeAvatarServiceModel;
import demoprojects.demo.service.models.bind.ProfileEditServiceModel;
import demoprojects.demo.service.models.view.UserProfileViewServiceModel;
import demoprojects.demo.web.models.ChangeAvatarModel;
import demoprojects.demo.web.models.PasswordChangeModel;
import demoprojects.demo.web.models.ProfileEditModel;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/mvn/users/api")
public class UserController extends BaseController {
    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper mapper;
    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;

    public UserController(UserService userService, RoleService roleService, ModelMapper mapper, CloudinaryService cloudinaryService, ImageService imageService) {
        this.userService = userService;
        this.roleService = roleService;
        this.mapper = mapper;
        this.cloudinaryService = cloudinaryService;
        this.imageService = imageService;
    }

    @GetMapping("/insert")
    public ModelAndView insert() {
        this.roleService.addNewRole();
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/delete/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteByUsername(@PathVariable("username") String username) {
        if (!this.userService.deactivateByUsername(username)) {
            super.redirect("/logout");//change it
        }

        return new ModelAndView("home/home");
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(@RequestParam String id, ModelAndView modelAndView) {
        UserProfileViewServiceModel userProfile = this.userService.getUserProfile(id);
        modelAndView.addObject("user", userProfile);
        modelAndView.addObject("hasAvatar", userProfile.getImgUrl() == null);
        modelAndView.setViewName("user/profile");

        return modelAndView;
    }

    @GetMapping("/profile/change-pass")
    public ModelAndView getChangePass(@RequestParam String id, ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("passChange")) {
            PasswordChangeModel passwordChangeModel = new PasswordChangeModel();
            passwordChangeModel.setId(id);
            model.addAttribute("passChange", passwordChangeModel);
        }
        modelAndView.setViewName("user/change-password");

        return modelAndView;
    }


    @PostMapping("/profile/change-pass")
    public ModelAndView getChangePassConfirm(@Valid @ModelAttribute("passChange")
                                                     PasswordChangeModel passChange,
                                             BindingResult bindingResult,
                                             RedirectAttributes redirectAttributes,
                                             @RequestParam String id,
                                             ModelAndView modelAndView) {

        boolean matching = passChange.getNewPassword()
                .equals(passChange.getConfirmNewPassword());

        boolean matchOldPassword = this.userService
                .isOldPasswordMatching(id, passChange.getOldPassword());
        if (bindingResult.hasErrors() || !matching || !matchOldPassword) {
            redirectAttributes.addFlashAttribute("passChange", passChange);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passChange", bindingResult);
            redirectAttributes.addFlashAttribute("notMatching", !matching);
            redirectAttributes.addFlashAttribute("notMatchingOld", !matchOldPassword);
            modelAndView.setViewName("redirect:/mvn/users/api/profile/change-pass?id=" + id);

        } else {
            this.userService.changePassword(id, passChange.getNewPassword());
            modelAndView.setViewName("redirect:/mvn/users/api/profile?id=" + id);
        }
        return modelAndView;
    }

    @GetMapping("/profile-view/{username}")
    public ModelAndView getProfileView(@PathVariable String username, ModelAndView modelAndView) {
        modelAndView.addObject("user", this.userService.getUserVIewProfile(username));
        modelAndView.setViewName("user/profile-view");

        return modelAndView;
    }

    @GetMapping("/profile/change-avatar")
    public ModelAndView changeAvatarPage(@RequestParam String id, ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("changeAvatar")) {
            ChangeAvatarModel changeAvatarModel = new ChangeAvatarModel();
            changeAvatarModel.setUserId(id);
            model.addAttribute("changeAvatar", changeAvatarModel);
        }
        modelAndView.setViewName("user/change-avatar");
        return modelAndView;
    }

    @PostMapping("/profile/change-avatar")
    public ModelAndView changeAvatarPageConfirm(@RequestParam String id,
                                                @ModelAttribute("changeAvatar") ChangeAvatarModel changeAvatarModel,
                                                RedirectAttributes redirectAttributes,
                                                ModelAndView modelAndView,
                                                Principal principal) throws IOException {
        boolean isMaxSizeCorrect = cloudinaryService.isFileSizeCorrect(changeAvatarModel.getImage());
        boolean isFormatCorrect = cloudinaryService.isFileFormatCorrect(changeAvatarModel.getImage());

        if (isMaxSizeCorrect && isFormatCorrect) {
            ChangeAvatarServiceModel map = this.mapper.map(changeAvatarModel, ChangeAvatarServiceModel.class);
            map.setFormat(changeAvatarModel.getImage().getContentType());
            map.setName(changeAvatarModel.getImage().getName());
            String previousProfileAvatarURL = this.userService.findPreviousAvatarURL(principal.getName());
            if (previousProfileAvatarURL != null) {
                this.cloudinaryService.delete(previousProfileAvatarURL);

            }
            map.setImgUrl(this.cloudinaryService.upload(changeAvatarModel.getImage()));
            this.imageService.uploadUserAvatar(map, id);
            modelAndView.setViewName("redirect:/mvn/users/api/profile?id=" + id);
        } else {
            modelAndView.addObject("incorrectSize", !isMaxSizeCorrect);
            modelAndView.addObject("incorrectFormat", !isFormatCorrect);
            modelAndView.setViewName("redirect:/mvn/users/api/profile/change-avatar?id=" + id);
        }

        return modelAndView;
    }

    @GetMapping("/profile/edit")
    public ModelAndView getEditProfile(@RequestParam String id, ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("user")) {
            modelAndView.addObject("user", this.mapper.map(this.userService.getEditUserProfile(id), ProfileEditModel.class));
        }

        modelAndView.setViewName("user/profile-edit");

        return modelAndView;
    }

    @PostMapping("/profile/edit")
    public ModelAndView getEditProfileConfirm(@Valid @ModelAttribute("user") ProfileEditModel user,
                                              BindingResult bindingResult,
                                              @RequestParam String id,
                                              RedirectAttributes redirectAttributes,
                                              ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            modelAndView.setViewName("redirect:/mvn/users/api/profile/edit?id=" + id);
        } else {
            ProfileEditServiceModel map = this.mapper.map(user, ProfileEditServiceModel.class);
            this.userService.editUserProfile(map.getId(), map);
        }

        modelAndView.setViewName("redirect:/mvn/users/api/profile?id=" + id);

        return modelAndView;
    }


}
