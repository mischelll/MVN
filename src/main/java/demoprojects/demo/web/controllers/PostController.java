package demoprojects.demo.web.controllers;

import demoprojects.demo.annottation.PageTitle;
import demoprojects.demo.dao.models.entities.PostCategoryName;
import demoprojects.demo.service.interfaces.blog.PostCommentService;
import demoprojects.demo.service.interfaces.blog.PostService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.service.models.bind.CommentCreateServiceModel;
import demoprojects.demo.service.models.view.PostCategoryCountModel;
import demoprojects.demo.service.models.bind.PostCreateServiceModel;
import demoprojects.demo.service.models.view.PostViewServiceModel;
import demoprojects.demo.util.constants.controllers.BlogConstants;
import demoprojects.demo.web.models.CommentCreateModel;
import demoprojects.demo.web.models.EditPostModel;
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

import static demoprojects.demo.util.constants.controllers.BlogConstants.*;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final ModelMapper mapper;
    private final PostCommentService commentService;
    private final UserService userService;

    public PostController(PostService postService, ModelMapper mapper, PostCommentService commentService, UserService userService) {
        this.postService = postService;
        this.mapper = mapper;
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/all")
    @PageTitle("Blog")
    public ModelAndView getAllPosts(Model model) {
        if (!model.containsAttribute("postSearch")) {
            model.addAttribute("postSearch", new PostSearchModel());
        }
        model.addAttribute("posts", this.postService.findLatest10());
        return new ModelAndView(INDEX_PAGE);
    }

    @GetMapping("/all/search")
    public ModelAndView searchFunction(@RequestParam String title, ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("postSearch")) {
            model.addAttribute("postSearch", new PostSearchModel());
        }
        String name = title;
        model.addAttribute("posts", this.postService.findByTitle(title));
        modelAndView.setViewName(INDEX_PAGE);

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
    @PageTitle("Blog | Create")
    public ModelAndView createPost(ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("postSearch")) {
            model.addAttribute("postSearch", new PostSearchModel());
        }
        if (!model.containsAttribute("post")) {
            model.addAttribute("post", new PostCreateModel());
        }
        modelAndView.setViewName(CREATE_PAGE);
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createPostConfirm(@Valid @ModelAttribute("post") PostCreateModel post,
                                          BindingResult bindingResult,
                                          RedirectAttributes redirectAttributes,
                                          ModelAndView modelAndView,
                                          Principal principal) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("post", post);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.post", bindingResult);
            modelAndView.setViewName(REDIRECT_CREATE_PAGE);
        } else {
            PostCreateServiceModel postServiceModel = this.mapper.map(post, PostCreateServiceModel.class);
            postServiceModel.setAuthor(principal.getName());

            PostCreateServiceModel model = this.postService.create(postServiceModel);
            if (model == null) {
                redirectAttributes.addFlashAttribute("post", post);
                redirectAttributes.addFlashAttribute("found", true);
                modelAndView.setViewName(REDIRECT_CREATE_PAGE);
            } else {
                modelAndView.setViewName(REDIRECT_ALL_POSTS_PAGE);
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
                                      RedirectAttributes redirectAttributes,
                                      Principal principal) {
        redirectAttributes.addAttribute("id", id);
        if (!model.containsAttribute("postSearch") || !model.containsAttribute("comment")) {
            model.addAttribute("comment", new CommentCreateModel());
            model.addAttribute("postSearch", new PostSearchModel());
        }
        List<PostCategoryCountModel> categories = new ArrayList<>();
        getCategoriesNames().forEach(categoryName -> categories
                .add(this.postService
                        .findPostsByCategory(categoryName.name())));
        PostViewServiceModel byId = this.postService.findById(id);
        modelAndView.addObject("hasAvatar", byId.getAuthorImgUrl() == null);
        modelAndView.addObject("isPrincipalOwner", byId.getAuthor().equals(principal.getName()));
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("popular", this.postService.getTopThreePosts());
        modelAndView.addObject("post", byId);
        modelAndView.addObject("comments", this.commentService.commentsOfPost(id));
        modelAndView.setViewName(SINGLE_PAGE);

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

        modelAndView.setViewName(REDIRECT_ARTICLE_PAGE + id);
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
        modelAndView.setViewName(ABOUT_PAGE);

        return modelAndView;
    }


    @GetMapping("/edit")
    public ModelAndView editPost(@RequestParam String id, Model model, ModelAndView modelAndView,Principal principal) {

        if (!model.containsAttribute("editPost")) {
            model.addAttribute("editPost", this.mapper.map(this.postService.findById(id), EditPostModel.class));
        }
        if (!model.containsAttribute("postSearch")) {
            model.addAttribute("postSearch", new PostSearchModel());
        }
        modelAndView.setViewName(EDIT_POST_PAGE);
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView editPostConfirm(@Valid @ModelAttribute("editPost") EditPostModel editPost,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes,
                                        ModelAndView modelAndView,
                                        @RequestParam String id) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("editPost", editPost);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editPost", bindingResult);
            modelAndView.setViewName(REDIRECT_EDIT_POST_PAGE + id);
        } else {
            PostCreateServiceModel map = this.mapper.map(editPost, PostCreateServiceModel.class);
            this.postService.edit(map, id);
            modelAndView.setViewName(REDIRECT_ALL_POSTS_PAGE);
        }

        return modelAndView;
    }


    @PostMapping("/become-moderator/{username}/{postID}")
    public ModelAndView subscribeForModerator(@PathVariable String username,@PathVariable String postID
            , ModelAndView modelAndView) {

        return modelAndView;
    }

    @GetMapping("/subscribe-newsletter/{username}/{postID}")
    @PageTitle("Blog Subscription Info")
    public ModelAndView subscribeForNewsletter(@PathVariable String username,@PathVariable String postID,
             ModelAndView modelAndView) {
        modelAndView.addObject("postID",postID);
        if (this.userService.isBlogSubscriptionSuccessful(username)) {
            modelAndView.setViewName(NEWSLETTER_SUCCESSFUL_PAGE);
        } else {
            modelAndView.addObject("postID",postID);

            modelAndView.setViewName(NEWSLETTER_UNSUCCESSFUL_PAGE);
        }
        return modelAndView;
    }

    @PostMapping("/delete")
    public ModelAndView deleteOwnPost(@RequestParam String id, Principal principal, ModelAndView modelAndView){
        PostViewServiceModel byId = this.postService.findById(id);
        if (byId.getAuthor().equals(principal.getName())){
            this.postService.deleteById(id);
            modelAndView.setViewName(REDIRECT_ALL_POSTS_PAGE);
        }else {
            modelAndView.setViewName(REDIRECT_ARTICLE_PAGE  +id);
        }

        return modelAndView;
    }
}
