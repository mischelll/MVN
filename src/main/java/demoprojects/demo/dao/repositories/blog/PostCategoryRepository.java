package demoprojects.demo.dao.repositories.blog;

import demoprojects.demo.dao.models.entities.PostCategory;
import demoprojects.demo.dao.models.entities.PostCategoryName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategory,String> {
    PostCategory findByName(PostCategoryName name);
}
