package diploma.rentapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import diploma.rentapp.domain.Address;
import diploma.rentapp.domain.ERole;
import diploma.rentapp.domain.User;
import diploma.rentapp.repository.UserRepository;
import diploma.rentapp.service.UserService;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    private User user1 = new User(
        "username1",
        "password",
        "Firstname",
        "Lastname",
        "user1@webshop.hu",
        "0036301234567",
        new Address("Country", "county", "city", "zipCode", "streetName", 1, "door"),
        null
    );
    private User user2 = new User(
        "username2",
        "password",
        "Firstname",
        "Lastname",
        "user2@webshop.hu",
        "0036301234567",
        new Address("Country", "county", "city", "zipCode", "streetName", 1, "door"),
        null
    );

    @BeforeEach
    public void before(){
        userRepository.deleteAll();
        userRepository.flush();
    }

    @Test
    public void userServiceContextLoads(){
        assertNotNull(userService);
    }

    @Test
    public void databaseEmptyTest(){
        boolean adminExists = userService.userExistsByRole(ERole.ROLE_ADMIN);
        assertFalse(adminExists);
    }

    @Test
    public void createAdminUserTest(){
        User result = userService.createAdminUser(user1);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    public void createUserTest(){
        User result = userService.createUser(user1);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    public void getUsersTest(){
        userService.createUser(user1);
        userService.createAdminUser(user2);
        List<User> users = userService.getUsers();
        assertEquals(2, users.size());
    }

    @Test
    public void getUserTest(){
        User created = userService.createUser(user1);
        User result = userService.getUser(created.getId());
        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
    }

    @Test
    public void getUserByUsernameTest(){
        userService.createUser(user1);
        User user = userService.getUserByUsername(user1.getUsername());
        assertNotNull(user);
    }

    @Test
    public void buyCartContentTest(){
    }

    @Test
    public void updateUserTest(){
        User created = userService.createUser(user1);
        User result = userService.updateUser(created, user2);
        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
        assertEquals(created.getUsername(), result.getUsername());
    }

    @Test
    public void deleteUserTest(){
        User user = userService.createUser(user1);
        userService.deleteUser(user.getId());
        List<User> users = userService.getUsers();
        assertEquals(0, users.size());
    }

    @Test
    public void deleteUserByUsernameTest(){
        User user = userService.createUser(user1);
        userService.deleteUserByUsername(user.getUsername());
        List<User> users = userService.getUsers();
        assertEquals(0, users.size());
    }
}
