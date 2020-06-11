package demoprojects.demo.service;

import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.service.models.UserLoginServiceModel;
import demoprojects.demo.service.models.UserRegisterServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User register(UserRegisterServiceModel user);
    boolean login(UserLoginServiceModel user);
    boolean deleteByUsername(String username);

}
