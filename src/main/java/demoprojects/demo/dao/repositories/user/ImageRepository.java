package demoprojects.demo.dao.repositories.user;

import demoprojects.demo.dao.models.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, String> {
}
