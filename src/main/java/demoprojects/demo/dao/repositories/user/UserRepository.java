package demoprojects.demo.dao.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import demoprojects.demo.dao.models.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    User findByUsername(String username);
    User findByEmail(String email);
}
