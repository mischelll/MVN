package demoprojects.demo.service.impl;

import demoprojects.demo.dao.models.entities.Category;
import demoprojects.demo.dao.models.entities.CategoryName;
import demoprojects.demo.dao.repositories.CategoryRepository;
import demoprojects.demo.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public void initCategories() {
        if (this.categoryRepository.count() == 0) {
            Arrays
                    .stream(CategoryName.values())
                    .forEach(categoryName -> {
                        this.categoryRepository.saveAndFlush(new Category(categoryName,
                                String.format("Some description for %s.", categoryName.name())));
                    });
        }
    }

    @Override
    public Category findByName(String name) {
        return this.categoryRepository.findByName(CategoryName.valueOf(name));
    }
}
