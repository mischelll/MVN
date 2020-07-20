package demoprojects.demo.dao.repositories.shop;

import demoprojects.demo.dao.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,String> {
}
