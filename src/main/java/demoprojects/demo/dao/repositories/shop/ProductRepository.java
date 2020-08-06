package demoprojects.demo.dao.repositories.shop;


import demoprojects.demo.dao.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, String> {
    Set<Product> findAllBySellerUsername(String username);

    Set<Product> findAllBySellerUsernameAndIsSoldIsTrueOrderBySold(String username);

    List<Product> findAllByBuyerUsernameOrderBySold(String username);

    List<Product> findAllByIsSoldFalseOrderByPrice();

    List<Product> findAllByIsSoldFalseAndTitleContainsOrderByCreatedDesc(String text);

    @Query(value = "SELECT P FROM Product P WHERE P.isSold = false " +
            "AND FUNCTION('DATEDIFF', P.created, current_date ) >= -7 " +
            "AND FUNCTION('DATEDIFF', P.created, current_date ) <= 0")
    List<Product> findAllByIsSoldFalseOrderByCreatedDesc();

    @Query(value = "SELECT COUNT(p.title) FROM some_db.products AS p" +
            " WHERE DATEDIFF(p.created_on,CURDATE()) >= -7 and  DATEDIFF(p.created_on,CURDATE()) <= 0", nativeQuery = true)
    Integer getLastWeekProductsCount();

    @Query(value = "SELECT P FROM Product P WHERE P.isSold = false " +
            "AND FUNCTION('DATEDIFF', P.created, current_date ) = -1")
    List<Product> findOneDayOldPorducts();
}
