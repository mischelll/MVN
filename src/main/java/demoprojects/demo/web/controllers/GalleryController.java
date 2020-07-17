package demoprojects.demo.web.controllers;

import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/gallery")
public class GalleryController {

    @GetMapping("/")
    public ModelAndView getIndex(ModelAndView modelAndView){
        modelAndView.setViewName("gallery/index");

        return modelAndView;
    }

    @GetMapping("/picture")
    public ModelAndView getPicture(ModelAndView modelAndView){
        modelAndView.setViewName("gallery/single");
        return modelAndView;
    }
}
