package demoprojects.demo.service.interfaces.blog;

import demoprojects.demo.dao.models.entities.PostCategory;

public interface PostCategoryService {
    void initCategories();

    PostCategory findByName(String name);

}
