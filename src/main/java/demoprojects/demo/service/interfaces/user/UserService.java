package demoprojects.demo.service.interfaces.user;

import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.service.models.bind.ProfileEditServiceModel;
import demoprojects.demo.service.models.bind.RoleChangeServiceModel;
import demoprojects.demo.service.models.bind.UserLoginServiceModel;
import demoprojects.demo.service.models.bind.UserRegisterServiceModel;
import demoprojects.demo.service.models.view.ProductViewServiceModel;
import demoprojects.demo.service.models.view.RoleViewServiceModel;
import demoprojects.demo.service.models.view.UserProfileViewServiceModel;
import demoprojects.demo.service.models.view.UserResponseModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserRegisterServiceModel register(UserRegisterServiceModel user);

    boolean login(UserLoginServiceModel user);

    boolean deactivateByUsername(String username);

    User findByUsername(String name);

    UserProfileViewServiceModel getUserProfile(String id);

    UserProfileViewServiceModel getUserVIewProfile(String username);

    void activateAccount(String username);

    List<UserResponseModel> listAll();

    List<RoleViewServiceModel> listRolesByUser(String username);

    boolean isEmailAvailable(String email);

    boolean isUsernameAvailable(String username);

    void changeRoles(RoleChangeServiceModel roles, String username);

    List<String> listAllUsernamesWithoutPrincipal(String principalUsername);

    String resetPassword(String email);

    boolean isOldPasswordMatching(String userId, String oldPassword);

    void changePassword(String userId, String newPassword);

    void activateByUsername(String username);

    List<UserResponseModel> listAllAdmins();

    ProfileEditServiceModel getEditUserProfile(String id);

    void editUserProfile(String id, ProfileEditServiceModel map);

    String getUserEmail(String username);

    List<String> listAllUserEmails();

    String findPreviousAvatarURL(String name);

    boolean isBlogSubscriptionSuccessful(String username);

    boolean isShopSubscriptionSuccessful(String username);

    List<String> findUsersSubscribedToBlog();

    List<String> findUsersSubscribedToShop();
}
