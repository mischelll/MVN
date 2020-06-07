package demoprojects.demo.service.impl;

import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.dao.repositories.UserRepository;
import demoprojects.demo.service.AuthServiceValidation;
import demoprojects.demo.service.UserService;
import demoprojects.demo.service.RoleService;
import demoprojects.demo.service.models.UserLoginServiceModel;
import demoprojects.demo.service.models.UserRegisterServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper mapper;
    private final AuthServiceValidation authServiceValidation;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper mapper, AuthServiceValidation authServiceValidation, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.mapper = mapper;
        this.authServiceValidation = authServiceValidation;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public boolean register(UserRegisterServiceModel user) {
        if (!authServiceValidation.isValid(user)) {
            return false;
        }

        User regUser = this.mapper.map(user, User.class);
        String pass = regUser.getPassword();
        regUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
        roleService.seedRolesInDb();

        if (this.userRepository.count() == 0) {
            regUser.setAuthorities(this.roleService.findAll());
        } else {
            regUser.setAuthorities(new LinkedHashSet<>());
            regUser.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
        }

        this.userRepository.saveAndFlush(regUser);
        return true;
    }

    @Override
    public boolean login(UserLoginServiceModel user) {
        User byUsername = this.userRepository.findByUsername(user.getUsername());
        if (byUsername == null || !this.passwordEncoder.matches(user.getPassword(),byUsername.getPassword())){
            return false;
        }
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username);
    }
}
