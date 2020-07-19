package demoprojects.demo.service.impl;

import demoprojects.demo.dao.models.entities.Gender;
import demoprojects.demo.dao.models.entities.Role;
import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.dao.repositories.UserRepository;
import demoprojects.demo.service.AuthServiceValidation;
import demoprojects.demo.service.PostService;
import demoprojects.demo.service.UserService;
import demoprojects.demo.service.RoleService;
import demoprojects.demo.service.models.bind.UserLoginServiceModel;
import demoprojects.demo.service.models.bind.UserRegisterServiceModel;
import demoprojects.demo.service.models.view.UserIdUsernameViewModel;
import demoprojects.demo.service.models.view.UserProfileViewServiceModel;
import demoprojects.demo.service.models.view.UserResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PostService postService;
    private final ModelMapper mapper;
    private final AuthServiceValidation authServiceValidation;
    private final PasswordEncoder passwordEncoder;
    private boolean isVerificationReady = false;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PostService postService, ModelMapper mapper, AuthServiceValidation authServiceValidation, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.postService = postService;
        this.mapper = mapper;
        this.authServiceValidation = authServiceValidation;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public UserRegisterServiceModel register(UserRegisterServiceModel user) {
        User regUser;

        regUser = this.mapper.map(user, User.class);
        regUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
        regUser.setGender(Gender.valueOf(user.getGender()));
        roleService.seedRolesInDb();

        if (this.userRepository.count() == 0) {
            regUser.setAuthorities(this.roleService.findAll());
        } else {
            regUser.setAuthorities(new LinkedHashSet<>());
            regUser.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
        }

        this.userRepository.saveAndFlush(regUser);
        return this.mapper.map(regUser, UserRegisterServiceModel.class);
    }


    @Override
    public boolean login(UserLoginServiceModel user) {
        User byUsername = this.userRepository.findByUsername(user.getUsername());
        if (byUsername == null || !this.passwordEncoder.matches(user.getPassword(), byUsername.getPassword())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteByUsername(String username) {
        User byUsername = this.userRepository.findByUsername(username);
        if (byUsername == null) {
            return false;
        }

        this.userRepository.delete(byUsername);
        return true;
    }

    @Override
    public User findByUsername(String name) {
        return this
                .userRepository
                .findByUsername(name);
    }

    @Override
    public UserProfileViewServiceModel getUserProfile(String id) {
        User user = this.userRepository.findById(id).orElse(null);
        UserProfileViewServiceModel map = this.mapper.map(user, UserProfileViewServiceModel.class);
        map.setFullName(user.getFirstName() + " " + user.getLastName());
        map.setRegisteredOn(user.getRegisteredOn().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy")));

        return map;
    }

    @Override
    public UserIdUsernameViewModel getUserHome(String username) {

        return this.mapper
                .map(this.userRepository
                                .findByUsername(username),
                        UserIdUsernameViewModel.class);

    }

    @Override
    public void activateAccount(String username) {
        User byUsername = this.userRepository.findByUsername(username);
        byUsername.setAccountNonExpired(true);
        byUsername.setAccountNonLocked(true);
        byUsername.setCredentialsNonExpired(true);
        byUsername.setEnabled(true);

        this.userRepository.saveAndFlush(byUsername);
    }

    @Override
    public List<UserResponseModel> listAll() {
        return this.userRepository
                .findAll()
                .stream().map(user -> {
                    UserResponseModel mapUser = this.mapper.map(user, UserResponseModel.class);
                    mapUser.setGender(user.getGender().toString());
                    mapUser.setRegisteredOn(user.getRegisteredOn().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
                    mapUser.setPostsCount(99);
                    mapUser.setRoles(user
                            .getAuthorities()
                            .stream()
                            .map(Role::getAuthority)
                            .collect(Collectors.joining(", ")));

                    return mapUser;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username);
    }
}
