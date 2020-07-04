package demoprojects.demo.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/shop")
public class ShopController extends BaseController{
    @GetMapping("/")
    public ModelAndView getShopIndex(ModelAndView modelAndView){
        modelAndView.setViewName("shop/index");
        return modelAndView;
    }

}