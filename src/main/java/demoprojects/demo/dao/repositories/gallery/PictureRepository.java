package demoprojects.demo.dao.repositories.gallery;

import demoprojects.demo.dao.models.entities.Picture;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PictureRepository extends JpaRepository<Picture,String> {
}
