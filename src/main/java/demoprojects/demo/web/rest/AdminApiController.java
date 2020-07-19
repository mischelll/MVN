package demoprojects.demo.web.rest;

import demoprojects.demo.service.PostService;
import demoprojects.demo.service.UserService;
import demoprojects.demo.service.models.view.UserResponseModel;
import demoprojects.demo.service.models.view.UserServiceModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/mvn/admin/api")
public class AdminApiController {
    private final UserService userService;
    private final PostService postService;

    public AdminApiController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/users")

    public List<UserResponseModel> listAllUsers(HttpSession session){
        return this.userService.listAll();
    }

}
