package demoprojects.demo.web.controllers;

import demoprojects.demo.service.PostService;
import demoprojects.demo.service.models.PostCreateServiceModel;
import demoprojects.demo.web.models.PostCreateModel;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

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
    public ModelAndView getPostsAbout() {
        return new ModelAndView("blog/about");
    }

    @GetMapping("/all")
    public ModelAndView getAllPosts(Model model) {
        model.addAttribute("posts", postService.findLatest10());
        return new ModelAndView("blog/index");
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN') or hasRole('ROLE_BLOG-KING')")
    public ModelAndView createPost(ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("post")) {
            model.addAttribute("post", new PostCreateModel());
        }
        modelAndView.setViewName("create-post");
        return modelAndView;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN') or hasRole('ROLE_BLOG-KING')")
    public ModelAndView createPostConfirm(@Valid @ModelAttribute("post") PostCreateModel post,
                                          BindingResult bindingResult,
                                          RedirectAttributes redirectAttributes,
                                          ModelAndView modelAndView,
                                          Principal principal) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("post", post);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.post", bindingResult);
            modelAndView.setViewName("redirect:/posts/create");
        } else {
            PostCreateServiceModel postServiceModel = this.mapper.map(post, PostCreateServiceModel.class);
            postServiceModel.setAuthor(principal.getName());

            PostCreateServiceModel model = this.postService.create(postServiceModel);
            if (model == null) {
                redirectAttributes.addFlashAttribute("post", post);
                redirectAttributes.addFlashAttribute("found", true);
                modelAndView.setViewName("redirect:/posts/create");
            } else {
                modelAndView.setViewName("redirect:/posts/all");
            }

        }
        return modelAndView;
    }

    @GetMapping("/see/all")
    public ModelAndView seeAll(Model model) {
        model.addAttribute("posts", postService.findLatest10());
        return new ModelAndView("blog");
    }
}
