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
import demoprojects.demo.util.constants.controllers.ShopConstants;
import demoprojects.demo.util.filesIO.FileManager;
import demoprojects.demo.web.models.*;
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
import static demoprojects.demo.util.constants.controllers.ShopConstants.*;

@Controller
@RequestMapping("/mvn/shop")
public class ShopController extends BaseController {
    private final ProductService productService;
    private final ModelMapper mapper;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;
    private final EmailService emailService;
    private final FileManager fileManager;


    public ShopController(ProductService productService, ModelMapper mapper,
                          UserService userService,
                          CloudinaryService cloudinaryService,
                          ImageService imageService,
                          EmailService emailService,
                          FileManager fileManager) {

        this.productService = productService;
        this.mapper = mapper;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.imageService = imageService;
        this.emailService = emailService;
        this.fileManager = fileManager;
    }

    @GetMapping("/")
    @PageTitle("Shop | Home")
    public ModelAndView getShopIndex(ModelAndView modelAndView,Model model) {
        if (!model.containsAttribute("productSearch")) {
            model.addAttribute("productSearch", new ProductSearchModel());
        }
        modelAndView.addObject("newestProducts", this.productService.listNewestProducts());
        modelAndView.setViewName(INDEX_PAGE);
        return modelAndView;
    }

    @GetMapping("/products")
    @PageTitle("Shop | Products")
    public ModelAndView getProducts(ModelAndView modelAndView,Model model) {
        if (!model.containsAttribute("productSearch")) {
            model.addAttribute("productSearch", new ProductSearchModel());
        }
        modelAndView.addObject("products", this.productService.listAllProducts());
        modelAndView.setViewName(PRODUCTS_PAGE);
        return modelAndView;
    }

    @GetMapping("/cart")
    public ModelAndView getCart(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/cart");

        return modelAndView;
    }

    @GetMapping("/about")
    @PageTitle("Shop | About")
    public ModelAndView getAbout(ModelAndView modelAndView,Model model) {
        if (!model.containsAttribute("productSearch")) {
            model.addAttribute("productSearch", new ProductSearchModel());
        }
        modelAndView.setViewName(CONTACT_PAGE);

        return modelAndView;
    }


    @GetMapping("/product/create")
    @PageTitle("Shop | Product | Create")
    public ModelAndView getProductCreate(ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("product")) {
            model.addAttribute("product", new ProductCreateModel());
        }
        if (!model.containsAttribute("productSearch")) {
            model.addAttribute("productSearch", new ProductSearchModel());
        }
        modelAndView.setViewName(CREATE_PRODUCT_PAGE);
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
            modelAndView.setViewName(REDIRECT_CREATE_PRODUCT_PAGE);
        } else {
            ProductCreateServiceModel productCreateServiceModel = this.mapper.map(product, ProductCreateServiceModel.class);
            productCreateServiceModel.setAuthor(principal.getName());
            ProductCreateServiceModel productCreate = this.productService.createProduct(productCreateServiceModel);
            modelAndView.setViewName(REDIRECT_IMAGES_UPLOAD_PAGE + productCreate.getId());
        }

        return modelAndView;
    }

    @PostMapping("/product/delete")
    public ModelAndView deleteProduct(@RequestParam String id, ModelAndView modelAndView) {
        this.productService.deleteById(id);
        modelAndView.setViewName(DELETE_PRODUCT_SUCCESS_PAGE);
        return modelAndView;
    }

    @GetMapping("/product")
    @PageTitle("Shop | Product")
    public ModelAndView getSingleProduct(@RequestParam String id, ModelAndView modelAndView, Model model, Principal principal) {
        if (!model.containsAttribute("productSearch")) {
            model.addAttribute("productSearch", new ProductSearchModel());
        }
        if (!model.containsAttribute("request")) {
            ProductRequestModel productRequestModel = new ProductRequestModel();
            model.addAttribute("request", productRequestModel);
        }
        this.productService.incrementViews(principal.getName(), id);
        modelAndView.addObject("product", this.productService.findProduct(id));
        modelAndView.addObject("relatedProducts", this.productService.findRelatedProducts(id));

        modelAndView.setViewName(PRODUCT_PAGE);
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
        modelAndView.setViewName(REDIRECT_SUCCESSFUL_REQUEST_PAGE + productId);

        return modelAndView;
    }

    @GetMapping("/successful-request")
    public ModelAndView getSuccessPage(ModelAndView modelAndView, @RequestParam String id) {
        modelAndView.addObject("id", id);
        modelAndView.setViewName(SUCCESSFUL_REQUEST_PAGE);

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

        modelAndView.setViewName(REDIRECT_PRODUCT_PAGE + id);
        return modelAndView;
    }


    @GetMapping("/product/edit")
    @PageTitle("Product Edit")
    public ModelAndView getEditProduct(@RequestParam String id, ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("productSearch")) {
            model.addAttribute("productSearch", new ProductSearchModel());
        }
        if (!model.containsAttribute("productEdit")) {
            model.addAttribute("productEdit", this.mapper.map(this.productService.findProductToEdit(id), ProductEditModel.class));
        }
        modelAndView.setViewName(EDIT_PRODUCT_PAGE);
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
            modelAndView.setViewName(REDIRECT_EDIT_PRODUCT_PAGE + id);
        } else {
            ProductEditServiceModel map = this.mapper.map(productEdit, ProductEditServiceModel.class);
            this.productService.editProduct(map, id);
            modelAndView.setViewName(REDIRECT_PRODUCT_PAGE + id);
        }

        return modelAndView;
    }

    @GetMapping("/my-products/{username}")
    @PageTitle("My Active Products")
    public ModelAndView getUserProducts(ModelAndView modelAndView, @PathVariable String username, Principal principal,Model model) {
        if (!model.containsAttribute("productSearch")) {
            model.addAttribute("productSearch", new ProductSearchModel());
        }
        if (!principal.getName().equals(username)) {
            modelAndView.setViewName(REDIRECT_MY_PRODUCTS_PAGE + principal.getName());
            return modelAndView;
        }
        modelAndView.addObject("user", username);
        modelAndView.addObject("usersList", this.userService.listAllUsernamesWithoutPrincipal(principal.getName()));
        modelAndView.setViewName(USER_PRODUCTS_PAGE);
        return modelAndView;
    }

    @GetMapping("/my-sold-products/{username}")
    @PageTitle("My Sold Products")
    public ModelAndView getUserSoldProducts(ModelAndView modelAndView, @PathVariable String username, Principal principal,Model model) {
        if (!model.containsAttribute("productSearch")) {
            model.addAttribute("productSearch", new ProductSearchModel());
        }
        if (!principal.getName().equals(username)) {
            modelAndView.setViewName(REDIRECT_MY_SOLD_PRODUCTS_PAGE + principal.getName());
            return modelAndView;
        }
        modelAndView.addObject("soldRevenue", this.productService.calculateSoldRevenue(username));
        modelAndView.addObject("user", username);
        modelAndView.setViewName(USER_SOLD_PRODUCTS_PAGE);
        return modelAndView;
    }

    @GetMapping("/my-bought-products/{username}")
    @PageTitle("My Bought Products")
    public ModelAndView getUserBoughtProducts(ModelAndView modelAndView, @PathVariable String username, Principal principal,Model model) {
        if (!model.containsAttribute("productSearch")) {
            model.addAttribute("productSearch", new ProductSearchModel());
        }
        if (!principal.getName().equals(username)) {
            modelAndView.setViewName(REDIRECT_MY_BOUGHT_PRODUCTS_PAGE + principal.getName());
            return modelAndView;
        }

        modelAndView.addObject("user", username);
        modelAndView.setViewName(USER_BOUGHT_PRODUCTS_PAGE);
        return modelAndView;
    }

    @GetMapping("/product/images/upload")
    @PageTitle("Images Upload")
    public ModelAndView uploadProductImages(@RequestParam String id, ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("productSearch")) {
            model.addAttribute("productSearch", new ProductSearchModel());
        }
        if (!model.containsAttribute("image")) {
            ProductImageCreateModel productImageCreateModel = new ProductImageCreateModel();
            productImageCreateModel.setProductId(id);
            model.addAttribute("image", productImageCreateModel);
        }

        modelAndView.setViewName(UPLOAD_PRODUCT_IMAGES_PAGE);
        return modelAndView;
    }

    @PostMapping("/product/images/upload")
    public ModelAndView uploadProductImagesConfirm(@Valid @ModelAttribute("image") ProductImageCreateModel image,
                                                   BindingResult bindingResult,
                                                   @RequestParam String id,
                                                   RedirectAttributes redirectAttributes,
                                                   ModelAndView modelAndView) {
        boolean fileFormatCorrect =fileManager.isFileFormatCorrect(image.getImages());
        boolean fileSizeCorrect = fileManager.isFileSizeCorrect(image.getImages());
        boolean fileCountCorrect = fileManager.isFileCountCorrect(image.getImages());

        if (bindingResult.hasErrors() || !fileFormatCorrect || !fileSizeCorrect || !fileCountCorrect) {
            redirectAttributes.addFlashAttribute("image", image);
            redirectAttributes.addFlashAttribute("maxFilesError", !fileCountCorrect);
            redirectAttributes.addFlashAttribute("maxFilesSizeError", !fileSizeCorrect);
            redirectAttributes.addFlashAttribute("filesFormatError", !fileFormatCorrect);

            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.image", bindingResult);
            modelAndView.setViewName(REDIRECT_IMAGES_UPLOAD_PRODUCT_PAGE + id);
        } else {
            image.getImages().forEach(imageCreate -> {
                ProductImageCreateServiceModel serviceModel = new ProductImageCreateServiceModel();
                serviceModel.setFormat(imageCreate.getContentType());
                serviceModel.setName(imageCreate.getOriginalFilename());
                try {
                    serviceModel.setImgUrl(this.cloudinaryService.upload(imageCreate));
                } catch (IOException e) {
                    modelAndView.addObject("productID",id);
                    modelAndView.setViewName(UNSUCCESSFUL_IMAGES_UPLOAD_PAGE);
                }
                this.imageService.uploadProductsPicture(serviceModel, id);
            });

            modelAndView.setViewName(REDIRECT_PRODUCTS_PAGE);
        }

        return modelAndView;
    }


    @GetMapping("/subscribe-newsletter/{username}")
    @PageTitle("Shop Subscription Info")
    public ModelAndView subscribeForNewsletter(@PathVariable String username,
                                               ModelAndView modelAndView) {

        if (this.userService.isShopSubscriptionSuccessful(username)) {
            modelAndView.setViewName(NEWSLETTER_SUCCESSFUL_PAGE);
        } else {

            modelAndView.setViewName(NEWSLETTER_UNSUCCESSFUL_PAGE);
        }
        return modelAndView;
    }

    @GetMapping("/product/search")
    @PageTitle("Product Search")
    public ModelAndView searchFunction(@RequestParam String title, ModelAndView modelAndView, Model model) {
        if (!model.containsAttribute("productSearch")) {
            model.addAttribute("productSearch", new ProductSearchModel());
        }
        model.addAttribute("products", this.productService.findByTitle(title));
        modelAndView.setViewName(PRODUCTS_PAGE);

        return modelAndView;
    }

    @PostMapping("/product/search")
    public ModelAndView searchFunctionConfirm(@Valid @ModelAttribute("product") ProductSearchModel product,
                                              BindingResult bindingResult,
                                              ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/mvn/shop/product/search");

        return modelAndView;
    }
}
