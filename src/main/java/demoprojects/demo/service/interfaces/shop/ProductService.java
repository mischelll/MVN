package demoprojects.demo.service.interfaces.shop;

import demoprojects.demo.service.models.bind.ProductCreateServiceModel;
import demoprojects.demo.service.models.bind.ProductEditServiceModel;
import demoprojects.demo.service.models.view.ProductNewResponseModel;
import demoprojects.demo.service.models.view.ProductViewServiceModel;
import demoprojects.demo.service.models.view.ProductsUserResponseModel;
import demoprojects.demo.web.models.ProductCreateModel;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductCreateServiceModel createProduct(ProductCreateServiceModel product);

    List<ProductNewResponseModel> listNewestProducts();

    List<ProductNewResponseModel> listAllProducts();

    ProductViewServiceModel findProduct(String id);

    void incrementViews(String username, String productId);

    List<ProductViewServiceModel> findRelatedProducts(String id);

    List<ProductViewServiceModel> findProductsByCategory(String category);

    List<ProductViewServiceModel> findProductsByUsername(String username);

    List<ProductsUserResponseModel> findSoldProductsByUsername(String username);

    List<ProductsUserResponseModel> findBoughtProductsByUsername(String username);

    void addProductToSold(String productId, String username);

    List<ProductsUserResponseModel> listProductsByUser(String username);

    ProductEditServiceModel editProduct(ProductEditServiceModel editServiceModel, String id);

    ProductEditServiceModel findProductToEdit(String id);

    void removeProductFromSold(String productId);

    BigDecimal calculateBoughtRevenue(String username);

    BigDecimal calculateSoldRevenue(String username);
}
