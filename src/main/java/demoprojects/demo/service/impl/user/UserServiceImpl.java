package demoprojects.demo.service.impl.user;

import demoprojects.demo.dao.models.entities.Gender;
import demoprojects.demo.dao.models.entities.Role;
import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.dao.repositories.user.UserRepository;
import demoprojects.demo.service.interfaces.blog.PostService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.service.interfaces.user.RoleService;
import demoprojects.demo.service.models.bind.ProfileEditServiceModel;
import demoprojects.demo.service.models.bind.RoleChangeServiceModel;
import demoprojects.demo.service.models.bind.UserLoginServiceModel;
import demoprojects.demo.service.models.bind.UserRegisterServiceModel;
import demoprojects.demo.service.models.view.RoleViewServiceModel;
import demoprojects.demo.service.models.view.UserProfileViewServiceModel;
import demoprojects.demo.service.models.view.UserResponseModel;
import org.apache.commons.lang3.RandomStringUtils;
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
        return byUsername != null && this.passwordEncoder.matches(user.getPassword(),
                byUsername.getPassword());
    }

    @Override
    public boolean deactivateByUsername(String username) {
        User byUsername = this.userRepository.findByUsername(username);
        if (byUsername == null) {
            return false;
        }
        byUsername.setEnabled(false);

        this.userRepository.saveAndFlush(byUsername);
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
        assert user != null;
        UserProfileViewServiceModel map = this.mapper.map(user, UserProfileViewServiceModel.class);
        map.setFullName(user.getFirstName() + " " + user.getLastName());
        map.setRegisteredOn(user.getRegisteredOn().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy")));
        map.setBio(user.getBio() == null ? "No bio" : user.getBio());
        map.setGender(user.getGender().name());
        map.setPostsSize(user.getPosts().size());
        map.setProductsSize(user.getOffers().size());

        map.setProductsSoldSize(user.getSoldProducts().size());
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
        roles.getRoles().forEach(el -> {
            Role byAuthority = this.roleService.findByAuthority(el);
            newRoles.add(byAuthority);
        });
        byUsername.setAuthorities(newRoles);

        this.userRepository.saveAndFlush(byUsername);
    }

    @Override
    public List<String> listAllUsernames() {
        List<String> names = new ArrayList<>();
        this.userRepository
                .findAll()
                .stream()
                .filter(User::isEnabled)
                .forEach(user -> names
                        .add(user.getUsername()));

        return names;
    }

    @Override
    public String resetPassword(String email) {
        User byEmail = this.userRepository.findByEmail(email);
        String newPassword = generateNewPassword();
        byEmail.setPassword(this.passwordEncoder.encode(newPassword));
        this.userRepository.saveAndFlush(byEmail);
        return newPassword;
    }

    @Override
    public boolean isOldPasswordMatching(String userId, String oldPassword) {
        User user = this.userRepository.findById(userId).orElse(null);
        assert user != null;
        return this.passwordEncoder.matches(oldPassword, user.getPassword());

    }

    @Override
    public void changePassword(String userId, String newPassword) {
        User user = this.userRepository.findById(userId).orElse(null);
        assert user != null;
        user.setPassword(this.passwordEncoder.encode(newPassword));

        this.userRepository.saveAndFlush(user);
    }

    @Override
    public void activateByUsername(String username) {
        User byUsername = this.userRepository.findByUsername(username);
        byUsername.setEnabled(true);

        this.userRepository.saveAndFlush(byUsername);
    }

    @Override
    public List<UserResponseModel> listAllAdmins() {
        return this.userRepository
                .findAll()
                .stream()
                .filter(user -> {

                    for (Role authority : user.getAuthorities()) {
                        if (authority.getAuthority().equals("ROLE_ADMIN")) {
                            return true;
                        }
                    }
                    return false;
                })
                .map(user -> {
                    UserResponseModel mapUser = this.mapper.map(user, UserResponseModel.class);
                    mapUser.setRegisteredOn(user.getRegisteredOn().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
                    mapUser.setActive(user.isEnabled() ? "Yes" : "No");
                    return mapUser;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProfileEditServiceModel getEditUserProfile(String id) {
        User user = this.userRepository.findById(id).orElse(null);
        ProfileEditServiceModel map = this.mapper.map(user, ProfileEditServiceModel.class);
        map.setFullName(user.getFirstName() + " " + user.getLastName());
        map.setRegisteredOn(user.getRegisteredOn().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy")));
        map.setGender(user.getGender().name());

        return map;
    }

    @Override
    public void editUserProfile(String id, ProfileEditServiceModel map) {
        User user = this.userRepository.findById(id).orElse(null);
        user.setBio(map.getBio());
        user.setEmail(map.getEmail());
        String[] split = map.getFullName().split("\\s+");
        if (split.length >= 2) {
            user.setFirstName(split[0]);
            user.setLastName(split[1]);
        }

        this.userRepository.saveAndFlush(user);
    }

    private String generateNewPassword() {
        Random random = new Random();
        List<String> special = List.of("!", "@", "#", "$", "%", "^", "&", "*");
        String thirdPart = special.get(random.nextInt(special.size()));
        String randomChars = RandomStringUtils.randomAlphabetic(2).toUpperCase();
        String firstPart = randomChars.substring(0, 1).toUpperCase() + randomChars.substring(1);
        String secondPart = RandomStringUtils.randomAlphabetic(5).toLowerCase();
        String numeric = RandomStringUtils.randomNumeric(3);

        return firstPart + secondPart + numeric + thirdPart;
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
