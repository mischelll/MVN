package demoprojects.demo.web.rest;

import demoprojects.demo.service.interfaces.shop.ProductService;
import demoprojects.demo.service.models.view.ProductNewServiceModel;
import demoprojects.demo.service.models.view.ProductsUserResponseModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
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
    public List<ProductNewServiceModel> listNewestProducts() throws InterruptedException {
        return this.productService.listNewestProducts();
    }

    @GetMapping("/my-products/{username}")
    @ResponseBody
    public ResponseEntity<List<ProductsUserResponseModel>> listUserProducts(@PathVariable String username,Principal principal) throws InterruptedException {
        if (principal.getName().equals(username)){
            List<ProductsUserResponseModel> productsUserResponseModels = this.productService.listProductsByUser(username);
            return new ResponseEntity<>(productsUserResponseModels,HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/my-products/sell/{productId}/{seller}/{username}")
    public ResponseEntity<Void> addProductToSold(@PathVariable String productId,@PathVariable String seller,@PathVariable String username, Principal principal, HttpServletResponse response) throws IOException {
        if (principal.getName().equals(seller)){
        this.productService.addProductToSold(productId,username);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/mvn/shop/my-products/" + principal.getName());
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/my-sold-products/{username}")
    @ResponseBody
    public ResponseEntity<List<ProductsUserResponseModel>> listUserSoldProducts(@PathVariable String username,Principal principal) throws InterruptedException {
        if (principal.getName().equals(username)) {
            List<ProductsUserResponseModel> soldProductsByUsername = this.productService.findSoldProductsByUsername(username);
            return new ResponseEntity<>(soldProductsByUsername, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    @PostMapping("/my-sold-products/backToMarket/{productId}")
    public ResponseEntity<Void> removeProductToSold(@PathVariable String productId, Principal principal)  {
        this.productService.removeProductFromSold(productId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/mvn/shop/my-products/" + principal.getName());
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/my-bought-products/{username}")
    @ResponseBody
    public ResponseEntity<List<ProductsUserResponseModel>> listUserBoughtProducts(@PathVariable String username,Principal principal) throws InterruptedException {
        if (principal.getName().equals(username)) {
            List<ProductsUserResponseModel> soldProductsByUsername =
                    this.productService.findBoughtProductsByUsername(username);
            return new ResponseEntity<>(soldProductsByUsername, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
