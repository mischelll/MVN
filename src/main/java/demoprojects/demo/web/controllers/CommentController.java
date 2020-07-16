package demoprojects.demo.web.controllers;

import demoprojects.demo.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/like")
    public ModelAndView likeComment(@RequestParam String postID,
                                    @RequestParam String commentId,
                                    ModelAndView modelAndView) {

        this.commentService.like(commentId);
        modelAndView.setViewName("redirect:/posts/article?id=" + postID);

        return modelAndView;
    }

    @GetMapping("/dislike")
    public ModelAndView dislikeComment(@RequestParam String postID,
                                    @RequestParam String commentId,
                                    ModelAndView modelAndView) {

        this.commentService.dislike(commentId);
        modelAndView.setViewName("redirect:/posts/article?id=" + postID);

        return modelAndView;
    }

    @GetMapping("/delete")
    public ModelAndView deleteComment(@RequestParam String postID,
                                       @RequestParam String commentId,
                                       ModelAndView modelAndView) {

        this.commentService.delete(commentId,postID);
        modelAndView.setViewName("redirect:/posts/article?id=" + postID);

        return modelAndView;
    }

}
