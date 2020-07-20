package demoprojects.demo.web.rest;

import demoprojects.demo.service.interfaces.blog.PostService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.service.models.view.PostResponseModel;
import demoprojects.demo.service.models.view.UserResponseModel;
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
    @ResponseBody
    public List<UserResponseModel> listAllUsers(HttpSession session){
        return this.userService.listAll();
    }

    @GetMapping("/posts")

    public List<PostResponseModel> listAllPosts(HttpSession session){
        List<PostResponseModel> postResponseModels = this.postService.listAll();
        int a  = 4;
        return this.postService.listAll();
    }


}
