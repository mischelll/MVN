package demoprojects.demo.service.interfaces.shop;


import demoprojects.demo.dao.models.entities.ProductCategory;
import demoprojects.demo.service.models.view.ProductCategoryViewModel;

import java.util.List;

public interface ProductCategoryService {
    void initCategories();

    ProductCategory findByName(String name);

    List<ProductCategoryViewModel> listAllCategories();
}
