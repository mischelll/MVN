package demoprojects.demo.dao.repositories.gallery;

import demoprojects.demo.dao.models.entities.PictureCategory;
import demoprojects.demo.dao.models.entities.PictureCategoryName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureCategoryRepository extends JpaRepository<PictureCategory,String> {
    PictureCategory findByName(PictureCategoryName name);
}
