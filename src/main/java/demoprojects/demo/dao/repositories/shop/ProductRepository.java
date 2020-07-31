package demoprojects.demo.dao.repositories.shop;

import demoprojects.demo.dao.models.entities.Product;
import demoprojects.demo.dao.models.entities.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, String> {
    Set<Product> findAllBySellerUsername(String username);

    Set<Product> findAllByBuyerUsernameAndIsSoldIsTrueOrderBySold(String username);

    List<Product> findAllByBuyerUsernameOrderBySold(String username);

    List<Product> findAllByIsSoldFalseOrderByPrice();

    List<Product> findAllByIsSoldFalseOrderByCreatedDesc();



}
