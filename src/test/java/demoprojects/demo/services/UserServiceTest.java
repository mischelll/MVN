package demoprojects.demo.services;

import demoprojects.demo.BaseTest;
import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.dao.repositories.user.UserRepository;
import demoprojects.demo.service.interfaces.blog.PostService;
import demoprojects.demo.service.interfaces.user.RoleService;
import demoprojects.demo.service.interfaces.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest extends BaseTest {
    @MockBean
    private RoleService roleService;
    @MockBean
    private PostService postService;


    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    private User user;

    private User getUser() {
        user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setPassword("Parola123321&");
        user.setUsername("badjo");
        user.setEmail("email@abv.bg");
        user.setFirstName("Badjo");
        user.setLastName("Badjov");
        user.setRegisteredOn(LocalDateTime.now());

        return user;
    }


    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_whenUserNotFound_shouldThrowException() {
        when(userRepository.findByUsername("bojo"))
                .thenThrow(UsernameNotFoundException.class)
                .thenReturn(Optional.empty());

        userService.loadUserByUsername("bojo");
    }

    @Test
    public void loadUserByUsername_whenUserFound_shouldReturnSameUser() {
        User user = getUser();

        when(userRepository.findByUsername("badjo"))
                .thenReturn(Optional.of(user));

        UserDetails loadedUser = userService.loadUserByUsername("badjo");

        assertEquals(user.getUsername(), loadedUser.getUsername());
        assertEquals(user.getPassword(), loadedUser.getPassword());
    }

}
