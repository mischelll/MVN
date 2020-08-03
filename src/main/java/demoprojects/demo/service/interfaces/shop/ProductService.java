package demoprojects.demo.service.interfaces.shop;

import demoprojects.demo.service.models.bind.ProductCreateServiceModel;
import demoprojects.demo.service.models.bind.ProductEditServiceModel;
import demoprojects.demo.service.models.view.ProductNewResponseModel;
import demoprojects.demo.service.models.view.ProductViewServiceModel;
import demoprojects.demo.service.models.view.ProductsUserResponseModel;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductCreateServiceModel createProduct(ProductCreateServiceModel product);

    List<ProductNewResponseModel> listNewestProducts();

    List<ProductViewServiceModel> listAllProducts();

    ProductViewServiceModel findProduct(String id);

    void incrementViews(String username, String productId);

    List<ProductViewServiceModel> findRelatedProducts(String id);

    List<ProductViewServiceModel> findProductsByCategory(String category);

    List<ProductsUserResponseModel> findSoldProductsByUsername(String username);

    List<ProductsUserResponseModel> findBoughtProductsByUsername(String username);

    void addProductToSold(String productId, String username);

    List<ProductsUserResponseModel> listProductsByUser(String username);

    void editProduct(ProductEditServiceModel editServiceModel, String id);

    ProductEditServiceModel findProductToEdit(String id);

    void removeProductFromSold(String productId);

    BigDecimal calculateSoldRevenue(String username);

    Integer getNewProductsCount();

    void deleteById(String id);
}
