package demoprojects.demo.web.controllers;

import demoprojects.demo.annottation.PageTitle;
import demoprojects.demo.service.interfaces.CloudinaryService;
import demoprojects.demo.service.interfaces.shop.ImageService;
import demoprojects.demo.service.interfaces.shop.ProductService;
import demoprojects.demo.service.interfaces.user.EmailService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.service.models.bind.ProductCreateServiceModel;
import demoprojects.demo.service.models.bind.ProductEditServiceModel;
import demoprojects.demo.service.models.bind.ProductImageCreateServiceModel;
import demoprojects.demo.web.models.ProductImageCreateModel;
import demoprojects.demo.web.models.ProductCreateModel;
import demoprojects.demo.web.models.ProductEditModel;
import demoprojects.demo.web.models.ProductRequestModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

import static demoprojects.demo.util.Messages.*;

@Controller
@RequestMapping("/mvn/shop")
public class ShopController extends BaseController {
    private final ProductService productService;
    private final ModelMapper mapper;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;
    private final EmailService emailService;


    public ShopController(ProductService productService, ModelMapper mapper,
                          UserService userService,
                          CloudinaryService cloudinaryService,
                          ImageService imageService,
                          EmailService emailService
    ) {

        this.productService = productService;
        this.mapper = mapper;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.imageService = imageService;
        this.emailService = emailService;

    }

    @GetMapping("/")
    @PageTitle("Shop | Home")
    public ModelAndView getShopIndex(ModelAndView modelAndView) {
        modelAndView.addObject("newestProducts", this.productService.listNewestProducts());
        modelAndView.setViewName("shop/index");
        return modelAndView;
    }

    @GetMapping("/products")
    @PageTitle("Shop | Products")
    public ModelAndView getProducts(ModelAndView modelAndView) {
        modelAndView.addObject("products", this.productService.listAllProducts());
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
            modelAndView.setViewName("redirect:/mvn/shop/product/images/upload?id=" + productCreate.getId());
        }

        return modelAndView;
    }

    @PostMapping("/product/delete")
    public ModelAndView deleteProduct(@RequestParam String id, ModelAndView modelAndView) {
        this.productService.deleteById(id);
        modelAndView.setViewName("shop/success-deletion");
        return modelAndView;
    }

    @GetMapping("/product")
    @PageTitle("Shop | Product")
    public ModelAndView getSingleProduct(@RequestParam String id, ModelAndView modelAndView, Model model, Principal principal) {
        if (!model.containsAttribute("request")) {
            ProductRequestModel productRequestModel = new ProductRequestModel();
            model.addAttribute("request", productRequestModel);
        }
        this.productService.incrementViews(principal.getName(), id);
        modelAndView.addObject("product", this.productService.findProduct(id));
        modelAndView.addObject("relatedProducts", this.productService.findRelatedProducts(id));

        modelAndView.setViewName("shop/product");
        return modelAndView;
    }

    @GetMapping("/product/request-sent/{productTitle}/{sellerEmail}/{client}/{sellerUsername}/{productId}")
    public ModelAndView sentRequestToUser(ModelAndView modelAndView,
                                          @PathVariable String productTitle,
                                          @PathVariable String sellerEmail,
                                          @PathVariable String client,
                                          @PathVariable String sellerUsername,
                                          @PathVariable String productId) {

        this.emailService.sendEmail(sellerEmail, interestInProductCC(sellerUsername, productTitle),
                interestInProductThroughUs(this.userService.getUserEmail(client), productTitle,
                        CONTACT_THROUGH_US_MESSAGE));
        modelAndView.setViewName("redirect:/mvn/shop/successful-request?id=" + productId);

        return modelAndView;
    }

    @GetMapping("/successful-request")
    public ModelAndView getSuccessPage(ModelAndView modelAndView, @RequestParam String id) {
        modelAndView.addObject("id", id);
        modelAndView.setViewName("shop/success-request");

        return modelAndView;
    }

    @PostMapping("/product/{productTitle}/{seller}/{email}")
    public ModelAndView sendRequestForProduct(@Valid @ModelAttribute("request") ProductRequestModel request,
                                              BindingResult bindingResult,
                                              @PathVariable String productTitle,
                                              @PathVariable String seller,
                                              @PathVariable String email,
                                              @RequestParam String id,
                                              RedirectAttributes redirectAttributes,
                                              ModelAndView modelAndView) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("request", request);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.request", bindingResult);
            redirectAttributes.addFlashAttribute("error", true);
        } else {
            this.emailService.sendEmail(email, interestInProductCC(seller, productTitle),
                    interestInProduct(request.getEmail(), productTitle, request.getText()));
            redirectAttributes.addFlashAttribute("success", true);
        }

        modelAndView.setViewName("redirect:/mvn/shop/product?id=" + id);
        return modelAndView;
    }


    @GetMapping("/product/edit")
    @PageTitle("Product Edit")
    public ModelAndView getEditProduct(@RequestParam String id, ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("productEdit")) {
            model.addAttribute("productEdit", this.mapper.map(this.productService.findProductToEdit(id), ProductEditModel.class));
        }
        modelAndView.setViewName("shop/product-edit");
        return modelAndView;
    }

    @PostMapping("/product/edit")
    public ModelAndView getEditProductConfirm(@Valid @ModelAttribute("productEdit") ProductEditModel productEdit,
                                              BindingResult bindingResult,
                                              RedirectAttributes redirectAttributes,
                                              @RequestParam String id,
                                              ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productEdit", productEdit);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productEdit", bindingResult);
            modelAndView.setViewName("redirect:/mvn/shop/product/edit?id=" + id);
        } else {
            ProductEditServiceModel map = this.mapper.map(productEdit, ProductEditServiceModel.class);
            this.productService.editProduct(map, id);
            modelAndView.setViewName("redirect:/mvn/shop/product?id=" + id);
        }

        return modelAndView;
    }

    @GetMapping("/my-products/{username}")
    @PageTitle("User Products")
    public ModelAndView getUserProducts(ModelAndView modelAndView, @PathVariable String username, Principal principal) {
        if (!principal.getName().equals(username)) {
            modelAndView.setViewName("redirect:/mvn/shop/my-products/" + principal.getName());
            return modelAndView;
        }
        modelAndView.addObject("user", username);
        modelAndView.addObject("usersList", this.userService.listAllUsernames());
        modelAndView.setViewName("shop/user-products");
        return modelAndView;
    }

    @GetMapping("/my-sold-products/{username}")
    @PageTitle("User Sold Products")
    public ModelAndView getUserSoldProducts(ModelAndView modelAndView, @PathVariable String username, Principal principal) {
        if (!principal.getName().equals(username)) {
            modelAndView.setViewName("redirect:/mvn/shop/my-sold-products/" + principal.getName());
            return modelAndView;
        }
        modelAndView.addObject("soldRevenue", this.productService.calculateSoldRevenue(username));
        modelAndView.addObject("user", username);
        modelAndView.setViewName("shop/user-sold-products");
        return modelAndView;
    }

    @GetMapping("/my-bought-products/{username}")
    public ModelAndView getUserBoughtProducts(ModelAndView modelAndView, @PathVariable String username, Principal principal) {
        if (!principal.getName().equals(username)) {
            modelAndView.setViewName("redirect:/mvn/shop/my-bought-products/" + principal.getName());
            return modelAndView;
        }

        modelAndView.addObject("user", username);
        modelAndView.setViewName("shop/user-bought-products");
        return modelAndView;
    }

    @GetMapping("/product/images/upload")
    @PageTitle("Images Upload")
    public ModelAndView uploadProductImages(@RequestParam String id, ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("image")) {
            ProductImageCreateModel productImageCreateModel = new ProductImageCreateModel();
            productImageCreateModel.setProductId(id);
            model.addAttribute("image", productImageCreateModel);
        }

        modelAndView.setViewName("shop/upload-product-images");
        return modelAndView;
    }

    @PostMapping("/product/images/upload")
    public ModelAndView uploadProductImagesConfirm(@Valid @ModelAttribute("image") ProductImageCreateModel image,
                                                   BindingResult bindingResult,
                                                   @RequestParam String id,
                                                   RedirectAttributes redirectAttributes,
                                                   ModelAndView modelAndView) {
        boolean maxFilesCheck = image.getImages().size() > 5;

        boolean filesSizeCheck;
        if (bindingResult.hasErrors() || maxFilesCheck) {
            redirectAttributes.addFlashAttribute("image", image);
            redirectAttributes.addFlashAttribute("maxFilesError", maxFilesCheck);

            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.image", bindingResult);
            modelAndView.setViewName("redirect:/mvn/shop/product/images/upload?id=" + id);
        } else {

            image.getImages().forEach(imageCreate -> {
                ProductImageCreateServiceModel serviceModel = new ProductImageCreateServiceModel();
                serviceModel.setFormat(imageCreate.getContentType());
                serviceModel.setName(imageCreate.getOriginalFilename());
                try {
                    serviceModel.setImgUrl(this.cloudinaryService.upload(imageCreate));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                this.imageService.uploadProductsPicture(serviceModel, id);
            });

            modelAndView.setViewName("redirect:/mvn/shop/products");
        }

        return modelAndView;
    }
}
