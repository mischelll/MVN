package demoprojects.demo.service;

import demoprojects.demo.service.models.bind.UserRegisterServiceModel;

public interface AuthServiceValidation {
    boolean isValid(UserRegisterServiceModel user);
}
