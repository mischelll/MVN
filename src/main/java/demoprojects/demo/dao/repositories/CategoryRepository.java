package demoprojects.demo.dao.repositories;

import demoprojects.demo.dao.models.entities.Category;
import demoprojects.demo.dao.models.entities.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,String> {
    Category findByName(CategoryName name);
}
