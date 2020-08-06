package demoprojects.demo.dao.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import demoprojects.demo.dao.models.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    User findByEmail(String email);

    @Query(value = "SELECT U.username  FROM User U WHERE U.enabled = true  AND U.username <>  :principalUsername")
    List<String> findAllWithoutPrincipal(String principalUsername);

    @Query(value = "SELECT  email from some_db.users", nativeQuery = true)
    List<String> getAllUserEmail();

    @Query(value = "SELECT U.email from User U where U.subscribedToBlog = true")
    List<String> getAllSubscribedToBlogUserEmails();

    @Query(value = "SELECT U.email from User U where U.subscribedToShop = true")
    List<String> getAllSubscribedToShopUserEmails();

    @Query(value = "SELECT U.avatar.imgUrl FROM User U WHERE U.avatar.imgUrl is not null AND U.username=:username")
    String findUserAvatarURL(String username);
}
