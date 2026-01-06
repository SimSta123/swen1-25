package at.technikum.application.mrp.user;

import at.technikum.application.mrp.authentification.AuthRepositoryC;
import at.technikum.application.mrp.authentification.AuthService;
import at.technikum.application.mrp.user.User;
import at.technikum.application.todo.exception.*;
import at.technikum.server.util.TokenStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepositoryC userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    public void get_noneFound_throwException(){

        assertThrows(EntityNotFoundException.class,
                () -> userService.get(1)
        );
    }

    @Test
    public  void update_noUsername_throwException(){
        User user = new User();
        user.setPassword("pw_123");

        assertThrows(IllegalArgumentException.class,
                () -> userService.update(user,1)
        );

        verifyNoInteractions(userRepository);
    }

    @Test
    public  void update_noPassword_throwException(){
        User user = new User();
        user.setUsername("user123");

        assertThrows(IllegalArgumentException.class,
                () -> userService.update(user,1)
        );

        verifyNoInteractions(userRepository);
    }

    @Test
    public  void update_emptyUsername_throwException(){
        User user = new User();
        user.setUsername("");
        user.setUsername("user123");

        assertThrows(IllegalArgumentException.class,
                () -> userService.update(user,1)
        );

        verifyNoInteractions(userRepository);
    }

    @Test
    public void update_emptyPasswort_throwException(){
        User user = new User();
        user.setUsername("user123");
        user.setUsername("");

        assertThrows(IllegalArgumentException.class,
                () -> userService.update(user,1)
        );

        verifyNoInteractions(userRepository);
    }
}
