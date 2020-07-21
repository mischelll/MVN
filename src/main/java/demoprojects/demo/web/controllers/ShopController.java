package demoprojects.demo.web.controllers;

import demoprojects.demo.service.interfaces.shop.ProductService;
import demoprojects.demo.service.models.bind.ProductCreateServiceModel;
import demoprojects.demo.web.models.ProductCreateModel;
import org.modelmapper.ModelMapper;
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
@RequestMapping("/mvn/shop")
public class ShopController extends BaseController {
    private final ProductService productService;
    private final ModelMapper mapper;

    public ShopController(ProductService productService, ModelMapper mapper) {
        this.productService = productService;
        this.mapper = mapper;
    }

    @GetMapping("/")
    public ModelAndView getShopIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/index");
        return modelAndView;
    }

    @GetMapping("/products")
    public ModelAndView getProducts(ModelAndView modelAndView) {
        modelAndView.addObject("products",productService.listNewestProducts());
        modelAndView.setViewName("shop/products");
        return modelAndView;
    }

    @GetMapping("/cart")
    public ModelAndView getCart(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/cart");

        return modelAndView;
    }

    @GetMapping("/about")
    public ModelAndView getAbout(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/contact");

        return modelAndView;
    }

    @GetMapping("/product")
    public ModelAndView getProduct(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/product");
        return modelAndView;
    }


    @GetMapping("/product/create")
    public ModelAndView getProductCreate(ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("product")) {
            model.addAttribute("product", new ProductCreateModel());
        }
        modelAndView.setViewName("shop/create-product");
        return modelAndView;
    }

    @PostMapping("/product/create")
    public ModelAndView getProductCreateConfirm(@Valid @ModelAttribute("product") ProductCreateModel product,
                                                BindingResult bindingResult,
                                                RedirectAttributes redirectAttributes,
                                                ModelAndView modelAndView,
                                                Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("product", product);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.product", bindingResult);
            modelAndView.setViewName("redirect:/mvn/shop/product/create");
        } else {
            ProductCreateServiceModel productCreateServiceModel = this.mapper.map(product, ProductCreateServiceModel.class);
            productCreateServiceModel.setAuthor(principal.getName());
            ProductCreateServiceModel productCreate = this.productService.createProduct(productCreateServiceModel);
            modelAndView.setViewName("redirect:/mvn/shop/products");
        }

        return modelAndView;
    }

}
