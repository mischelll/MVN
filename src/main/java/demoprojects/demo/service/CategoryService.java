package demoprojects.demo.service;

import demoprojects.demo.dao.models.entities.Category;

public interface CategoryService {
    void initCategories();

    Category findByName(String name);

}
