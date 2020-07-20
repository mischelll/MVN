package demoprojects.demo.service.impl.blog;

import demoprojects.demo.dao.models.entities.PostCategory;
import demoprojects.demo.dao.models.entities.PostCategoryName;
import demoprojects.demo.dao.repositories.blog.PostCategoryRepository;
import demoprojects.demo.service.interfaces.blog.PostCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class PostCategoryServiceImpl implements PostCategoryService {
    private final PostCategoryRepository categoryRepository;
    private final ModelMapper mapper;

    public PostCategoryServiceImpl(PostCategoryRepository categoryRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public void initCategories() {
        if (this.categoryRepository.count() < PostCategoryName.values().length) {
            Arrays
                    .stream(PostCategoryName.values())
                    .forEach(categoryName -> {
                        if(this.categoryRepository.findByName(categoryName) == null){
                            this.categoryRepository.saveAndFlush(new PostCategory(categoryName,
                                    String.format("Some description for %s.", categoryName.name())));
                        }

                    });
        }
    }

    @Override
    public PostCategory findByName(String name) {
        return this.categoryRepository.findByName(PostCategoryName.valueOf(name));
    }
}
