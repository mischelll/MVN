package demoprojects.demo.web.controllers;

import demoprojects.demo.annottation.PageTitle;
import demoprojects.demo.dao.models.entities.PostCategoryName;
import demoprojects.demo.service.interfaces.blog.PostCommentService;
import demoprojects.demo.service.interfaces.blog.PostService;
import demoprojects.demo.service.models.bind.CommentCreateServiceModel;
import demoprojects.demo.service.models.view.PostCategoryCountModel;
import demoprojects.demo.service.models.bind.PostCreateServiceModel;
import demoprojects.demo.web.models.CommentCreateModel;
import demoprojects.demo.web.models.PostCreateModel;
import demoprojects.demo.web.models.PostSearchModel;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final ModelMapper mapper;
    private final PostCommentService commentService;

    public PostController(PostService postService, ModelMapper mapper, PostCommentService commentService) {
        this.postService = postService;
        this.mapper = mapper;
        this.commentService = commentService;
    }

    @GetMapping("/all")
    @PageTitle("Blog")
    public ModelAndView getAllPosts(Model model) {
        if (!model.containsAttribute("postSearch")) {
            model.addAttribute("postSearch", new PostSearchModel());
        }
        model.addAttribute("posts", this.postService.findLatest10());
        return new ModelAndView("blog/index");
    }

    @GetMapping("/all/search")
    public ModelAndView searchFunction(@RequestParam String title, ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("postSearch")) {
            model.addAttribute("postSearch", new PostSearchModel());
        }
        String name = title;
        model.addAttribute("posts", this.postService.findByTitle(title));
        modelAndView.setViewName("blog/index");

        return modelAndView;
    }

    @PostMapping("/all/search")
    public ModelAndView searchFunctionConfirm(@Valid @ModelAttribute("post") PostSearchModel post,
                                              BindingResult bindingResult,
                                              ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/posts/all/search");

        return modelAndView;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN') or hasRole('ROLE_BLOG-KING')")
    @PageTitle("Blog | Create")
    public ModelAndView createPost(ModelAndView modelAndView, Model model) {
        if(!model.containsAttribute("postSearch")){
            model.addAttribute("postSearch", new PostSearchModel());
        }
        if (!model.containsAttribute("post")) {
            model.addAttribute("post", new PostCreateModel());
        }
        modelAndView.setViewName("blog/create-post");
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

    @GetMapping("/article")
    @PageTitle("Article")
    public ModelAndView getSinglePost(ModelAndView modelAndView, @RequestParam String id,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("id", id);
        if (!model.containsAttribute("postSearch") || !model.containsAttribute("comment")) {
            model.addAttribute("comment",new CommentCreateModel());
            model.addAttribute("postSearch", new PostSearchModel());
        }
        List<PostCategoryCountModel> categories = new ArrayList<>();
        getCategoriesNames().forEach(categoryName -> categories
                .add(this.postService
                        .findPostsByCategory(categoryName.name())));

        modelAndView.addObject("categories", categories);
        modelAndView.addObject("popular", this.postService.getTopThreePosts());
        modelAndView.addObject("post", this.postService.findById(id));
        modelAndView.addObject("comments",this.commentService.commentsOfPost(id));
        modelAndView.setViewName("blog/single");

        return modelAndView;
    }

    @PostMapping("/article")
    public ModelAndView createComment(@Valid @ModelAttribute("comment") CommentCreateModel comment,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      ModelAndView modelAndView,
                                      Principal principal,
                                      @RequestParam String id) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("comment", comment);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.comment", bindingResult);
        } else {
            CommentCreateServiceModel map = this.mapper.map(comment, CommentCreateServiceModel.class);
            map.setAuthor(principal.getName());
            map.setDate(LocalDateTime.now());
            map.setPostID(id);

            CommentCreateServiceModel comment1 =
                    this.commentService.createComment(map);
        }

        modelAndView.setViewName("redirect:/posts/article?id=" + id);
        return modelAndView;
    }

    private List<PostCategoryName> getCategoriesNames() {
        return Arrays.asList(PostCategoryName.values());
    }

    @GetMapping("/about")
    @PageTitle("Blog | About")
    public ModelAndView getAbout(ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("postSearch")) {
            model.addAttribute("postSearch", new PostSearchModel());
        }
        modelAndView.setViewName("blog/about");

        return modelAndView;
    }

    @GetMapping("/delete/{title}")
    public ModelAndView deletePost(@PathVariable String title, ModelAndView modelAndView){
        this.postService.deleteByTitle(title);
        modelAndView.setViewName("blog/index");
        return modelAndView;
    }
}
