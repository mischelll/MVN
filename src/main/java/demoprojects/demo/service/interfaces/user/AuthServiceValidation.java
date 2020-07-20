package demoprojects.demo.service.interfaces.user;

import demoprojects.demo.service.models.bind.UserRegisterServiceModel;

public interface AuthServiceValidation {
    boolean isValid(UserRegisterServiceModel user);
}
