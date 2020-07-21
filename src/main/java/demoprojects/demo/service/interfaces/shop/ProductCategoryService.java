package demoprojects.demo.service.interfaces.shop;


import demoprojects.demo.dao.models.entities.ProductCategory;

public interface ProductCategoryService {
    void initCategories();

    ProductCategory findByName(String name);
}
