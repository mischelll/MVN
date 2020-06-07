package demoprojects.demo.service;

import demoprojects.demo.service.models.UserLoginServiceModel;
import demoprojects.demo.service.models.UserRegisterServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    boolean register(UserRegisterServiceModel user);
    boolean login(UserLoginServiceModel user);
}
