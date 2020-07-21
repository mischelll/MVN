package demoprojects.demo.web.rest;

import demoprojects.demo.service.interfaces.shop.ProductService;
import demoprojects.demo.service.models.view.ProductNewResponseModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mvn/shop/api")
public class ShopApiController {
    private final ProductService productService;

    public ShopApiController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/products")
    @ResponseBody
    public List<ProductNewResponseModel> listNewestProducts(){
        return this.productService.listNewestProducts();
    }
}
