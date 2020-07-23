package demoprojects.demo.web.controllers;

import demoprojects.demo.annottation.PageTitle;
import demoprojects.demo.service.interfaces.shop.ProductService;
import demoprojects.demo.service.models.bind.ProductCreateServiceModel;
import demoprojects.demo.service.models.view.ProductViewServiceModel;
import demoprojects.demo.web.models.ProductCreateModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    @PageTitle("Shop | Home")
    public ModelAndView getShopIndex(ModelAndView modelAndView) {
        modelAndView.addObject("newestProducts",this.productService.listNewestProducts());
        modelAndView.setViewName("shop/index");
        return modelAndView;
    }

    @GetMapping("/products")
    @PageTitle("Shop | Products")
    public ModelAndView getProducts(ModelAndView modelAndView) {
        modelAndView.addObject("products", productService.listNewestProducts());
        modelAndView.setViewName("shop/products");
        return modelAndView;
    }

    @GetMapping("/cart")
    public ModelAndView getCart(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/cart");

        return modelAndView;
    }

    @GetMapping("/about")
    @PageTitle("Shop | About")
    public ModelAndView getAbout(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/contact");

        return modelAndView;
    }


    @GetMapping("/product/create")
    @PageTitle("Shop | Product | Create")
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

    @GetMapping("/product")
    @PageTitle("Shop | Product")
    public ModelAndView getSingleProduct(@RequestParam String id, ModelAndView modelAndView,Principal principal) {
        this.productService.incrementViews(principal.getName(),id);
        modelAndView.addObject("product",this.productService.findProduct(id));
        modelAndView.addObject("relatedProducts", this.productService.findRelatedProducts(id));
        modelAndView.setViewName("shop/product");
        return modelAndView;
    }
}
