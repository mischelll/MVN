package demoprojects.demo.dao.repositories.blog;

import demoprojects.demo.dao.models.entities.PostComment;
import demoprojects.demo.dao.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, String> {

}
