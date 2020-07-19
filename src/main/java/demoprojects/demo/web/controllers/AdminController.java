package demoprojects.demo.web.controllers;

import demoprojects.demo.annottation.PageTitle;
import demoprojects.demo.service.PostService;
import demoprojects.demo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/mvn/admin")
public class AdminController {
    private final UserService userService;
    private final PostService postService;

    public AdminController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
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
        modelAndView.setViewName("admin/index");
        return modelAndView;
    }
    @GetMapping("/shop")
    @PageTitle("Shop Settings")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public ModelAndView getShoSettings(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/index");
        return modelAndView;
    }
}
