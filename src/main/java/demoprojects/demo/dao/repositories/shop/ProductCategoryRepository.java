package demoprojects.demo.dao.repositories.shop;


import demoprojects.demo.dao.models.entities.PostCategoryName;
import demoprojects.demo.dao.models.entities.ProductCategory;
import demoprojects.demo.dao.models.entities.ProductCategoryName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,String> {
   ProductCategory findByName(ProductCategoryName name);
}
