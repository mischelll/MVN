package demoprojects.demo.dao.repositories.gallery;

import demoprojects.demo.dao.models.entities.PictureComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureCommentRepository extends JpaRepository<PictureComment,String> {
}
