package demoprojects.demo.service.impl.shop;

import demoprojects.demo.dao.models.entities.Product;
import demoprojects.demo.dao.models.entities.ProductCategory;
import demoprojects.demo.dao.repositories.shop.ProductRepository;
import demoprojects.demo.service.interfaces.shop.ProductCategoryService;
import demoprojects.demo.service.interfaces.shop.ProductService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.service.models.bind.ProductCreateServiceModel;
import demoprojects.demo.service.models.view.ProductNewResponseModel;
import demoprojects.demo.service.models.view.ProductViewServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    public ProductViewServiceModel findProduct(String id) {
        Product product = this.productRepository.findById(id).orElse(null);
        assert product != null;

        ProductViewServiceModel map = this.mappper.map(product, ProductViewServiceModel.class);
        map.setCreated(product.getCreated().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm")));
        map.setFullName(product.getSeller().getFirstName() + " " + product.getSeller().getLastName());
        map.setEmail(product.getSeller().getEmail());
        map.setUsername(product.getSeller().getUsername());

        return map;
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

        return generateProducts(category.getProducts());
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

    private List<ProductViewServiceModel> generateProducts(Set<Product> products) {
        return products
                .stream()
                .sorted((a, b) -> {
                    int sort = b.getCreated().compareTo(a.getCreated());
                    if (sort == 0) {
                        sort = b.getPrice().compareTo(a.getPrice());
                    }
                    return sort;
                })
                .map(product -> {
                    ProductViewServiceModel productViewServiceModel = this.mappper.map(product, ProductViewServiceModel.class);
                    productViewServiceModel.setCreated(product.getCreated().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm")));
                    productViewServiceModel.setFullName(product.getSeller().getFirstName() + " " + product.getSeller().getLastName());
                    productViewServiceModel.setEmail(product.getSeller().getEmail());
                    productViewServiceModel.setUsername(product.getSeller().getUsername());

                    return productViewServiceModel;
                })
                .collect(Collectors.toList());
    }
}
