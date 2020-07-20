package demoprojects.demo.service.impl.shop;

import demoprojects.demo.dao.models.entities.PostCategory;
import demoprojects.demo.dao.models.entities.PostCategoryName;
import demoprojects.demo.dao.models.entities.ProductCategory;
import demoprojects.demo.dao.models.entities.ProductCategoryName;
import demoprojects.demo.dao.repositories.shop.ProductCategoryRepository;
import demoprojects.demo.service.interfaces.shop.ProductCategoryService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public void initCategories() {
        if (this.productCategoryRepository.count() < ProductCategoryName.values().length) {
            Arrays
                    .stream(ProductCategoryName.values())
                    .forEach(categoryName -> {
                        if(this.productCategoryRepository.findByName(categoryName) == null){
                            this.productCategoryRepository.saveAndFlush(new ProductCategory(categoryName,
                                    String.format("Some description for %s.", categoryName.name())));
                        }

                    });
        }
    }
}
