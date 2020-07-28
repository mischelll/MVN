package demoprojects.demo.service.impl.shop;

import demoprojects.demo.dao.models.entities.Product;
import demoprojects.demo.dao.models.entities.ProductCategory;
import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.dao.repositories.shop.ProductRepository;
import demoprojects.demo.service.interfaces.shop.ProductCategoryService;
import demoprojects.demo.service.interfaces.shop.ProductService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.service.models.bind.ProductCreateServiceModel;
import demoprojects.demo.service.models.bind.ProductEditServiceModel;
import demoprojects.demo.service.models.view.ProductNewResponseModel;
import demoprojects.demo.service.models.view.ProductViewServiceModel;
import demoprojects.demo.service.models.view.ProductsUserResponseModel;
import demoprojects.demo.service.models.view.UserResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;
    private final ModelMapper mappper;
    private final UserService userService;

    public ProductServiceImpl(ProductRepository productRepository, ProductCategoryService productCategoryService, ModelMapper mappper, UserService userService) {
        this.productRepository = productRepository;
        this.productCategoryService = productCategoryService;
        this.mappper = mappper;
        this.userService = userService;
    }

    @Override
    public ProductCreateServiceModel createProduct(ProductCreateServiceModel product) {
        Product product1 = this.mappper.map(product, Product.class);
        product1.setSeller(this.userService.findByUsername(product.getAuthor()));
        product1.setCreated(LocalDateTime.now().withNano(0));
        product1.setComments(new HashSet<>());
        Set<ProductCategory> categories = new HashSet<>();
        product.getCategory().forEach(productCategoryName -> {
            categories.add(this.productCategoryService.findByName(productCategoryName.name()));
        });
        product1.setCategories(categories);
        return this
                .mappper
                .map(this.productRepository
                                .saveAndFlush(product1),
                        ProductCreateServiceModel.class);
    }

    @Override
    public List<ProductNewResponseModel> listNewestProducts() {
        return this.productRepository
                .findAll()
                .stream()
                .filter(product -> !product.getIsSold())
                .sorted((a, b) -> b.getCreated().compareTo(a.getCreated()))
                .limit(16)
                .map(product -> {
                    ProductNewResponseModel map = this.mappper.map(product, ProductNewResponseModel.class);
                    map.setFullName(product.getSeller().getFirstName() + " " + product.getSeller().getLastName());
                    map.setUsername(product.getSeller().getUsername());
                    map.setCreated(product.getCreated().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy")));

                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductNewResponseModel> listAllProducts() {
        return this.productRepository
                .findAll()
                .stream()
                .filter(product -> !product.getIsSold())
                .sorted(Comparator.comparing(Product::getPrice))
                .map(product -> {
                    ProductNewResponseModel map = this.mappper.map(product, ProductNewResponseModel.class);
                    map.setFullName(product.getSeller().getFirstName() + " " + product.getSeller().getLastName());
                    map.setUsername(product.getSeller().getUsername());
                    map.setCreated(product.getCreated().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm")));
                    map.setEmail(product.getSeller().getEmail());
                    map.setContactNumber(product.getTelephoneNumber());
                    List<String> cat = new ArrayList<>();
                    product.getCategories().forEach(category -> cat.add(category.getName().name()));
                    map.setCategories(String.join(", ", cat));

                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductViewServiceModel findProduct(String id) {
        Product product = this.productRepository.findById(id).orElse(null);
        assert product != null;

        return mapEntity(product);
    }

    @Override
    public void incrementViews(String username, String productId) {
        Product product =
                this.productRepository.findById(productId).orElse(null);

        assert product != null;
        if (!product.getSeller().getUsername().equals(username)) {
            product.setViews(product.getViews() + 1);
            this.productRepository.saveAndFlush(product);
        }
    }

    @Override
    public List<ProductViewServiceModel> findRelatedProducts(String id) {
        Product product1 = this.productRepository.findById(id).orElse(null);
        assert product1 != null;
        ProductCategory category = product1.getCategories().iterator().next();
        return category
                .getProducts()
                .stream()
                .filter(product -> !product.getIsSold() && !product.getId().equals(id))
                .sorted((a, b) -> {
                    int sort = b.getCreated().compareTo(a.getCreated());
                    if (sort == 0) {
                        sort = b.getPrice().compareTo(a.getPrice());
                    }
                    return sort;
                })
                .map(this::mapEntity)
                .collect(Collectors.toList());

    }

    @Override
    public List<ProductViewServiceModel> findProductsByCategory(String category) {
        ProductCategory byName = this.productCategoryService.findByName(category);
        return generateProducts(byName.getProducts());
    }

    @Override
    public List<ProductViewServiceModel> findProductsByUsername(String username) {
        Set<Product> allBySellerUsername = this.productRepository
                .findAllBySellerUsername(username);
        return generateProducts(allBySellerUsername);
    }

    @Override
    public List<ProductsUserResponseModel> findSoldProductsByUsername(String username) {
        Set<Product> allBySellerUsername = this.productRepository
                .findAllBySellerUsername(username);
        return allBySellerUsername
                .stream()
                .filter(Product::getIsSold)
                .sorted((a, b) -> b.getSold().compareTo(a.getSold()))
                .map(product -> {
                    ProductsUserResponseModel map = this.mappper.map(product,
                            ProductsUserResponseModel.class);

                    map.setBuyerUsername(product.getBuyer().getUsername());
                    map.setSoldOnDate(product.getSold().format(
                            DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm")));

                    return map;
                })
                .collect(Collectors.toList());
    }


    @Override
    public void addProductToSold(String productId, String username) {
        Product product = this.productRepository.findById(productId).orElse(null);
        assert product != null;

        User buyer = this.userService.findByUsername(username);
        product.setIsSold(true);
        product.setBuyer(buyer);
        product.setSold(LocalDateTime.now().withNano(0));
        buyer.getBoughtProducts().add(product);

        User seller = this.userService.findByUsername(product.getSeller().getUsername());
        seller.getSoldProducts().add(product);

        this.productRepository.saveAndFlush(product);
    }

    @Override
    public List<ProductsUserResponseModel> listProductsByUser(String username) {
        return this.productRepository
                .findAllBySellerUsername(username)
                .stream()
                .filter(product -> !product.getIsSold())
                .map(product -> {
                    ProductsUserResponseModel map = this.mappper.map(product, ProductsUserResponseModel.class);
                    map.setCreatedOnDate(product.getCreated().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm")));
                    List<UserResponseModel> userResponseModels = this.userService.listAll();


                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductEditServiceModel editProduct(ProductEditServiceModel editServiceModel, String id) {
        Product product = this.productRepository.findById(id).orElse(null);

        Set<ProductCategory> categories = new HashSet<>();
        editServiceModel.getCategory().forEach(productCategoryName -> {
            ProductCategory byName = this.productCategoryService.findByName(productCategoryName.name());
            categories.add(byName);
        });
        product.setCategories(categories);
        product.setPreview(editServiceModel.getPreview());
        product.setPrice(editServiceModel.getPrice());
        product.setTitle(editServiceModel.getTitle());
        product.setTelephoneNumber(editServiceModel.getTelephoneNumber());
        product.setDescription(editServiceModel.getDescription());

        this.productRepository.saveAndFlush(product);
        return editServiceModel;
    }

    @Override
    public ProductEditServiceModel findProductToEdit(String id) {
        Product product = this.productRepository.findById(id).orElse(null);
        assert product != null;
        return this.mappper.map(product,ProductEditServiceModel.class);
    }

    @Override
    public void removeProductFromSold(String productId) {
        Product product = this.productRepository.findById(productId).orElse(null);
        product.setIsSold(false);
        product.getBuyer().getBoughtProducts().remove(product);
        product.setBuyer(null);
        product.setSold(null);

        User byUsername = this.userService.findByUsername(product.getSeller().getUsername());
        byUsername.getSoldProducts().remove(product);

        this.productRepository.saveAndFlush(product);
    }

    private List<ProductViewServiceModel> generateProducts(Set<Product> products) {
        return products
                .stream()
                .filter(product -> !product.getIsSold())
                .sorted((a, b) -> {
                    int sort = b.getCreated().compareTo(a.getCreated());
                    if (sort == 0) {
                        sort = b.getPrice().compareTo(a.getPrice());
                    }
                    return sort;
                })
                .map(this::mapEntity)
                .collect(Collectors.toList());
    }

    private ProductViewServiceModel mapEntity(Product product) {
        ProductViewServiceModel productViewServiceModel = this.mappper.map(product, ProductViewServiceModel.class);
        productViewServiceModel.setCreated(product.getCreated().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm")));
        productViewServiceModel.setFullName(product.getSeller().getFirstName() + " " + product.getSeller().getLastName());
        productViewServiceModel.setEmail(product.getSeller().getEmail());
        productViewServiceModel.setUsername(product.getSeller().getUsername());
        return productViewServiceModel;
    }
}
