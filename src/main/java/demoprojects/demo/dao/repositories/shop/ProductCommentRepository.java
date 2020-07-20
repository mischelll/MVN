package demoprojects.demo.dao.repositories.shop;

import demoprojects.demo.dao.models.entities.ProductComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCommentRepository extends JpaRepository<ProductComment, String> {
}
