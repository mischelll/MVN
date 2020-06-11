package demoprojects.demo.web.controllers;

import demoprojects.demo.service.PostService;
import demoprojects.demo.service.models.PostServiceModel;
import demoprojects.demo.web.models.PostCreateModel;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final ModelMapper mapper;

    public PostController(PostService postService, ModelMapper mapper) {
        this.postService = postService;
        this.mapper = mapper;
    }

    @GetMapping("/about")
    public ModelAndView getPostsAbout(){
        return new ModelAndView("blog/about");
    }

    @GetMapping("/all")
    public ModelAndView getAllPosts(Model model) {
        model.addAttribute("posts",postService.findLatest10());
        return new ModelAndView("blog/index");
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN') or hasRole('ROLE_BLOG-KING')")
    public ModelAndView createPost() {

        return new ModelAndView("create-post");
    }
    @GetMapping("/see/all")
    public ModelAndView seeAll(Model model){
        model.addAttribute("posts",postService.findLatest10());
        return new ModelAndView("blog");
    }

    @PostMapping("/create")
    public ModelAndView createPostConfirm(@Valid @ModelAttribute("post") PostCreateModel post, BindingResult bindingResult, Model model) {
        model.addAttribute("post",post);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        post.setAuthor(username);
        PostServiceModel map = this.mapper.map(post, PostServiceModel.class);
        this.postService.create(map);

        return new ModelAndView("redirect:/posts/all");
    }
}
