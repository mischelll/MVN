package demoprojects.demo.service;

import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.service.models.bind.UserLoginServiceModel;
import demoprojects.demo.service.models.bind.UserRegisterServiceModel;
import demoprojects.demo.service.models.view.UserIdUsernameViewModel;
import demoprojects.demo.service.models.view.UserProfileViewServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserRegisterServiceModel register(UserRegisterServiceModel user);

    boolean login(UserLoginServiceModel user);

    boolean deleteByUsername(String username);

    User findByUsername(String name);

    UserProfileViewServiceModel getUserProfile(String id);

    UserIdUsernameViewModel getUserHome(String username);

    void activateAccount(String username);
}
