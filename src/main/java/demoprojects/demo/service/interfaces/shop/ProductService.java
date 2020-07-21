package demoprojects.demo.service.interfaces.shop;

import demoprojects.demo.service.models.bind.ProductCreateServiceModel;
import demoprojects.demo.service.models.view.ProductNewResponseModel;
import demoprojects.demo.web.models.ProductCreateModel;

import java.util.List;

public interface ProductService {
    ProductCreateServiceModel createProduct(ProductCreateServiceModel product);

    List<ProductNewResponseModel> listNewestProducts();
}