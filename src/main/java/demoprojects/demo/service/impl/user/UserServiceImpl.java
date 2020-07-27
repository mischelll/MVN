package demoprojects.demo.service.impl.user;

import demoprojects.demo.dao.models.entities.Gender;
import demoprojects.demo.dao.models.entities.Role;
import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.dao.repositories.user.UserRepository;
import demoprojects.demo.service.interfaces.blog.PostCommentService;
import demoprojects.demo.service.interfaces.user.AuthServiceValidation;
import demoprojects.demo.service.interfaces.blog.PostService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.service.interfaces.user.RoleService;
import demoprojects.demo.service.models.bind.RoleChangeServiceModel;
import demoprojects.demo.service.models.bind.UserLoginServiceModel;
import demoprojects.demo.service.models.bind.UserRegisterServiceModel;
import demoprojects.demo.service.models.view.RoleViewServiceModel;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PostService postService;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PostService postService, ModelMapper mapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.postService = postService;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public UserRegisterServiceModel register(UserRegisterServiceModel user) {
        User regUser = this.mapper.map(user, User.class);

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
    public UserProfileViewServiceModel getUserVIewProfile(String username) {
        User user = this.userRepository.findByUsername(username);
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
                .stream()
                .map(user -> {
                    UserResponseModel mapUser = this.mapper.map(user, UserResponseModel.class);
                    mapUser.setGender(user.getGender().toString());
                    mapUser.setRegisteredOn(user.getRegisteredOn().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
                    mapUser.setPostsCount(99);
                    mapUser.setActive(user.isEnabled() ? "Yes" : "No");
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
    public boolean isEmailAvailable(String email) {
        boolean check = true;
        if (this.userRepository.findByEmail(email) != null) {
            check = false;
        }
        return check;
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        boolean check = true;
        if (this.userRepository.findByUsername(username) != null) {
            check = false;
        }
        return check;
    }

    @Override
    public void changeRoles(RoleChangeServiceModel roles, String username) {
        User byUsername = this.userRepository.findByUsername(username);
        Set<Role> newRoles = new HashSet<>();
        roles.getRoles().forEach(el ->{
            Role byAuthority = this.roleService.findByAuthority(el);
            newRoles.add(byAuthority);
        });
        byUsername.setAuthorities(newRoles);

        this.userRepository.saveAndFlush(byUsername);
    }

    @Override
    public List<String> listAllUsernames() {
        List<String> names = new ArrayList<>();
       this.userRepository.findAll().forEach(user -> {
           names.add(user.getUsername());
       });

       return names;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User byUsername = this.userRepository.findByUsername(username);
        if (byUsername == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        return this.userRepository.findByUsername(username);


    }

    @Override
    public List<RoleViewServiceModel> listRolesByUser(String username) {
        User byUsername = this.userRepository.findByUsername(username);
        return byUsername
                .getAuthorities()
                .stream()
                .map(role -> {
                    RoleViewServiceModel map = this.mapper.map(role, RoleViewServiceModel.class);
                    map.setAuthority(role.getAuthority());

                    return map;
                })
                .collect(Collectors.toList());
    }
}
