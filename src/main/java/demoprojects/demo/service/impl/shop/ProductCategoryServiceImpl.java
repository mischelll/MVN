package demoprojects.demo.service.impl.shop;

import demoprojects.demo.dao.models.entities.ProductCategory;
import demoprojects.demo.dao.models.entities.ProductCategoryName;
import demoprojects.demo.dao.repositories.shop.ProductCategoryRepository;
import demoprojects.demo.service.interfaces.shop.ProductCategoryService;
import demoprojects.demo.service.models.view.ProductCategoryViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ModelMapper mapper;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository, ModelMapper mapper) {
        this.productCategoryRepository = productCategoryRepository;
        this.mapper = mapper;
    }

    @Override
    public void initCategories() {
        if (this.productCategoryRepository.count() < ProductCategoryName.values().length) {
            Arrays
                    .stream(ProductCategoryName.values())
                    .forEach(categoryName -> {
                        if (this.productCategoryRepository.findByName(categoryName) == null) {
                            this.productCategoryRepository.saveAndFlush(new ProductCategory(categoryName,
                                    String.format("Some description for %s.", categoryName.name())));
                        }

                    });
        }
    }

    @Override
    public ProductCategory findByName(String name) {
        return this
                .productCategoryRepository
                .findByName(ProductCategoryName.valueOf(name));
    }

    @Override
    public List<ProductCategoryViewModel> listAllCategories() {

        return this.productCategoryRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(a -> a.getName().name()))
                .map(category -> this.mapper.map(category, ProductCategoryViewModel.class))
                .collect(Collectors.toList());
    }
}
