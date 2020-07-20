package demoprojects.demo.init;

import demoprojects.demo.service.interfaces.blog.PostCategoryService;
import demoprojects.demo.service.interfaces.gallery.PictureCategoryService;
import demoprojects.demo.service.interfaces.shop.ProductCategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CategoryInit implements CommandLineRunner {
    private final PostCategoryService categoryService;
    private final ProductCategoryService productCategoryService;
    private final PictureCategoryService pictureCategoryService;

    public CategoryInit(PostCategoryService categoryService,
                        ProductCategoryService productCategoryService,
                        PictureCategoryService pictureCategoryService) {
        this.categoryService = categoryService;
        this.productCategoryService = productCategoryService;
        this.pictureCategoryService = pictureCategoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.categoryService.initCategories();
        this.pictureCategoryService.initCategories();
        this.productCategoryService.initCategories();
    }
}
