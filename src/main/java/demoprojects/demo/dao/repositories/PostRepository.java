package demoprojects.demo.dao.repositories;

import demoprojects.demo.dao.models.entities.Post;
import demoprojects.demo.dao.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    Post findByTitle(String title);

    Post findByAuthor(User author);

    List<Post> findAllByTitleContains(String text);

}
