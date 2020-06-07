package demoprojects.demo.service;

import demoprojects.demo.service.models.UserRegisterServiceModel;

public interface AuthServiceValidation {
    boolean isValid(UserRegisterServiceModel user);
}
