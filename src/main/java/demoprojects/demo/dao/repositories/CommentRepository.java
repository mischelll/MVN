package demoprojects.demo.dao.repositories;

import demoprojects.demo.dao.models.entities.Comment;
import demoprojects.demo.dao.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository  extends JpaRepository<Comment, String> {
    Comment findByAuthor(User author);
}
