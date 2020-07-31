package demoprojects.demo.web.controllers;

import demoprojects.demo.annottation.PageTitle;
import demoprojects.demo.service.interfaces.blog.PostService;
import demoprojects.demo.service.interfaces.user.RoleService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.service.models.bind.RoleChangeServiceModel;
import demoprojects.demo.web.models.RoleChangeModel;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/mvn/admin")
public class AdminController {
    private final UserService userService;
    private final PostService postService;
    private final RoleService roleService;
    private final ModelMapper mapper;

    public AdminController(UserService userService, PostService postService, RoleService roleService, ModelMapper mapper) {
        this.userService = userService;
        this.postService = postService;
        this.roleService = roleService;
        this.mapper = mapper;
    }

    @GetMapping("/")
    @PageTitle("Admin Panel")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public ModelAndView getAdminPanel(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/index");
        return modelAndView;
    }
    @GetMapping("/users")
    @PageTitle("User Management")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public ModelAndView getUsers(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/users");
        return modelAndView;
    }
    @GetMapping("/blog")
    @PageTitle("Blog Settings")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public ModelAndView getBlogSettings(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/blog-settings");
        return modelAndView;
    }
    @GetMapping("/shop")
    @PageTitle("Shop Settings")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public ModelAndView getShopSettings(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/shop-settings");
        return modelAndView;
    }

    @GetMapping("/gallery")
    @PageTitle("Gallery Settings")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public ModelAndView getGallerySettings(ModelAndView modelAndView) {
        modelAndView.setViewName("gallery/construction");
        return modelAndView;
    }


    @GetMapping("/edit-role/{username}")
    @PageTitle("User Role Edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public ModelAndView getEditRole(@PathVariable String username, ModelAndView modelAndView, Model model){
        if (!model.containsAttribute("roleChange")){
            model.addAttribute("roleChange",new RoleChangeModel());
        }
        modelAndView.addObject("roles",this.userService.listRolesByUser(username));
        modelAndView.addObject("rolesAll",this.roleService.listAllRoles());
        modelAndView.addObject("user",this.userService.getUserVIewProfile(username));
        modelAndView.setViewName("user/edit-role");
        return modelAndView;
    }

    @PostMapping("/edit-role/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public ModelAndView getEditRoleConfirm(@ModelAttribute("roleChange") RoleChangeModel roleChange,@PathVariable String username, ModelAndView modelAndView){
       this.userService.changeRoles(this.mapper.map(roleChange, RoleChangeServiceModel.class),username);
        modelAndView.setViewName("redirect:/mvn/admin/users");
        return modelAndView;
    }
}
