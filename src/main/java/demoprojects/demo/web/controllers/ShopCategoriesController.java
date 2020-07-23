package demoprojects.demo.web.controllers;

import demoprojects.demo.annottation.PageTitle;
import demoprojects.demo.service.interfaces.shop.ProductCategoryService;
import demoprojects.demo.service.interfaces.shop.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/mvn/shop/categories")
public class ShopCategoriesController {

    private final ProductCategoryService productCategoryService;
    private final ProductService productService;

    public ShopCategoriesController(ProductCategoryService productCategoryService, ProductService productService) {
        this.productCategoryService = productCategoryService;
        this.productService = productService;
    }

    @GetMapping("")
    @PageTitle("Categories")
    public ModelAndView getCategoryPage(ModelAndView modelAndView) {
        modelAndView.addObject("categories", this.productCategoryService.listAllCategories());
        modelAndView.setViewName("shop/categories");

        return modelAndView;
    }

    @GetMapping("/{category}")
    @PageTitle("Category")
    public ModelAndView getCategory(ModelAndView modelAndView, @PathVariable String category) {
        modelAndView.addObject("categoryName", category);
        modelAndView.addObject("productsByCategory", this.productService.findProductsByCategory(category));
        modelAndView.setViewName("shop/categories/category-products");
        return modelAndView;
    }
}
