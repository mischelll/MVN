package demoprojects.demo.service;

import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.service.models.UserLoginServiceModel;
import demoprojects.demo.service.models.UserRegisterServiceModel;
import demoprojects.demo.web.models.UserRegisterModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserRegisterServiceModel register(UserRegisterServiceModel user);
    boolean login(UserLoginServiceModel user);
    boolean deleteByUsername(String username);

}
