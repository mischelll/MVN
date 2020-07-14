package demoprojects.demo.service.impl;

import demoprojects.demo.dao.repositories.UserRepository;
import demoprojects.demo.service.AuthServiceValidation;
import demoprojects.demo.service.models.bind.UserRegisterServiceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthServiceValidationImpl implements AuthServiceValidation {
    private final UserRepository userRepository;
    private static final String EMAIL_REGEX = "^[A-Za-z_.]+@[a-z]+\\.[a-z]{2,4}$";
    private static final String PASSWORD_REGEX = "^(?<pass>[A-Z]{1,}[a-z]{1,}[0-9]+)$";
    @Autowired
    public AuthServiceValidationImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(UserRegisterServiceModel user) {
        return isEmailValid(user.getEmail())
                && isUsernameValid(user.getUsername())
                && isPasswordValid(user.getPassword())
                && arePasswordsMatching(user.getPassword(), user.getConfirmPassword());
    }

    private boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isUsernameValid(String username) {
        boolean minChars  = username.length() >= 3 && username.length() <= 32;
        boolean unique = this.userRepository.findByUsername(username) == null;

        return minChars && unique;

    }

    private boolean isPasswordValid(String password) {
        boolean minChars = password.length() >= 8 && password.length() <= 255;
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        boolean match = matcher.matches();
        return minChars && match;
    }

    private boolean arePasswordsMatching(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
