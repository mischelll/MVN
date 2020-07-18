package demoprojects.demo.web.controllers;

import demoprojects.demo.annottation.PageTitle;
import demoprojects.demo.service.PostService;
import demoprojects.demo.web.models.PostSearchModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/posts/categories")
public class PostCategoriesController {
private final PostService postService;

    public PostCategoriesController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/travel")
    @PageTitle("Blog | Travel")
    public ModelAndView getTravel(ModelAndView modelAndView, Model model){
        if (!model.containsAttribute("postSearch")) {
            model.addAttribute("postSearch", new PostSearchModel());
        }
        modelAndView.addObject("posts",postService.findByCategories("TRAVEL"));//constant
        modelAndView.setViewName("blog/categories-pages/travel");

        return modelAndView;

    }
    @GetMapping("/lifestyle")
    @PageTitle("Blog | Lifestyle")
    public ModelAndView getLifestyle(ModelAndView modelAndView, Model model){
        if (!model.containsAttribute("postSearch")) {
            model.addAttribute("postSearch", new PostSearchModel());
        }
        modelAndView.addObject("posts",postService.findByCategories("LIFESTYLE"));//constant
        modelAndView.setViewName("blog/categories-pages/lifestyle");

        return modelAndView;

    }
    @GetMapping("/sport")
    @PageTitle("Blog | Sport")
    public ModelAndView getSport(ModelAndView modelAndView, Model model){
        if (!model.containsAttribute("postSearch")) {
            model.addAttribute("postSearch", new PostSearchModel());
        }
        modelAndView.addObject("posts",postService.findByCategories("SPORT"));//constant
        modelAndView.setViewName("blog/categories-pages/sport");

        return modelAndView;

    }
    @GetMapping("/music")
    @PageTitle("Blog | Music")
    public ModelAndView getMusic(ModelAndView modelAndView, Model model){
        if (!model.containsAttribute("postSearch")) {
            model.addAttribute("postSearch", new PostSearchModel());
        }
        modelAndView.addObject("posts",postService.findByCategories("MUSIC"));//constant
        modelAndView.setViewName("blog/categories-pages/music");

        return modelAndView;

    }
    @GetMapping("/it")
    @PageTitle("Blog | IT")
    public ModelAndView getIT(ModelAndView modelAndView, Model model){
        if (!model.containsAttribute("postSearch")) {
            model.addAttribute("postSearch", new PostSearchModel());
        }
        modelAndView.addObject("posts",postService.findByCategories("IT"));//constant
        modelAndView.setViewName("blog/categories-pages/it");

        return modelAndView;

    }
    @GetMapping("/cars")
    @PageTitle("Blog | About")
    public ModelAndView getCars(ModelAndView modelAndView, Model model){
        if (!model.containsAttribute("postSearch")) {
            model.addAttribute("postSearch", new PostSearchModel());
        }
        modelAndView.addObject("posts",postService.findByCategories("CARS"));//constant
        modelAndView.setViewName("blog/categories-pages/cars");

        return modelAndView;

    }
}
