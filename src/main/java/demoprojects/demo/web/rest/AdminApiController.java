package demoprojects.demo.web.rest;

import demoprojects.demo.service.interfaces.blog.PostService;
import demoprojects.demo.service.interfaces.shop.ProductService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.service.models.view.PostResponseModel;
import demoprojects.demo.service.models.view.ProductNewResponseModel;
import demoprojects.demo.service.models.view.UserResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/mvn/admin/api")
public class AdminApiController {
    private final UserService userService;
    private final PostService postService;
    private final ProductService productService;

    public AdminApiController(UserService userService, PostService postService, ProductService productService) {
        this.userService = userService;
        this.postService = postService;
        this.productService = productService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ROOT') or hasRole('ROLE_ADMIN')")
    @ResponseBody
    public ResponseEntity<List<UserResponseModel>> listAllUsers(HttpSession session) {
        List<UserResponseModel> userResponseModels = this.userService.listAll();
        return new ResponseEntity<>(userResponseModels,HttpStatus.OK);
    }

    @GetMapping("/posts")
    @PreAuthorize("hasRole('ROLE_ROOT') or hasRole('ROLE_ADMIN')")
    public List<PostResponseModel> listAllPosts(HttpSession session) {
        List<PostResponseModel> postResponseModels = this.postService.listAll();
        int a = 4;
        return this.postService.listAll();
    }

    @PostMapping("/posts/delete")
    @PreAuthorize("hasRole('ROLE_ROOT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_BLOG-KING')")
    public ResponseEntity<Void> deletePost(@RequestParam String id){
        this.postService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/products")
    @PreAuthorize("hasRole('ROLE_ROOT') or hasRole('ROLE_ADMIN')")
    public List<ProductNewResponseModel> listAllProducts(HttpSession session) {
        List<ProductNewResponseModel> productNewResponseModels = this.productService.listAllProducts();
        int a = 4;
        return this.productService.listAllProducts();
    }

    @PostMapping("/users/deactivate/{username}")
    @PreAuthorize("hasRole('ROLE_ROOT') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deactivateUser(@PathVariable String username)  {
        this.userService.deactivateByUsername(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/users/activate/{username}")
    @PreAuthorize("hasRole('ROLE_ROOT') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> activateUser(@PathVariable String username)  {
        this.userService.activateByUsername(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
