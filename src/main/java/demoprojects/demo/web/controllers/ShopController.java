package demoprojects.demo.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/shop")
public class ShopController extends BaseController {
    @GetMapping("/")
    public ModelAndView getShopIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/index");
        return modelAndView;
    }

    @GetMapping("/categories")
    public ModelAndView getCategories(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/categories");
        return modelAndView;
    }

    @GetMapping("/cart")
    public ModelAndView getCart(ModelAndView modelAndView){
        modelAndView.setViewName("shop/cart");

        return modelAndView;
    }

    @GetMapping("/about")
    public ModelAndView getAbout(ModelAndView modelAndView){
        modelAndView.setViewName("shop/contact");

        return modelAndView;
    }

    @GetMapping("/product")
    public ModelAndView getProduct(ModelAndView modelAndView){
        modelAndView.setViewName("shop/product");

        return modelAndView;
    }



}
