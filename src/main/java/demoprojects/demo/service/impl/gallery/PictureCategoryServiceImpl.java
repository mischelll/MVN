package demoprojects.demo.service.impl.gallery;

import demoprojects.demo.dao.models.entities.PictureCategory;
import demoprojects.demo.dao.models.entities.PictureCategoryName;
import demoprojects.demo.dao.models.entities.ProductCategory;
import demoprojects.demo.dao.models.entities.ProductCategoryName;
import demoprojects.demo.dao.repositories.gallery.PictureCategoryRepository;
import demoprojects.demo.service.interfaces.gallery.PictureCategoryService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class PictureCategoryServiceImpl implements PictureCategoryService {
    private final PictureCategoryRepository pictureCategoryRepository;

    public PictureCategoryServiceImpl(PictureCategoryRepository pictureCategoryRepository) {
        this.pictureCategoryRepository = pictureCategoryRepository;
    }

    @Override
    public void initCategories() {
        if (this.pictureCategoryRepository.count() < PictureCategoryName.values().length) {
            Arrays
                    .stream(PictureCategoryName.values())
                    .forEach(categoryName -> {
                        if(this.pictureCategoryRepository.findByName(categoryName) == null){
                            this.pictureCategoryRepository.saveAndFlush(new PictureCategory(categoryName,
                                    String.format("Some description for %s.", categoryName.name())));
                        }

                    });
        }

    }
}
