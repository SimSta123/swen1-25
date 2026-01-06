package at.technikum.application.mrp.authentification;

import at.technikum.application.mrp.rating.RatingRepositoryC;
import at.technikum.application.mrp.rating.RatingService;
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
public class AuthServiceTest {

    @Mock
    private AuthRepositoryC authRepository;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(authRepository);
    }

    @Test
    public void createUserTest_success() throws IllegalArgumentException {
        User user = new User();
        user.setPassword("pw123");
        user.setUsername("name123");

        User user2 = new User();
        user2.setUsername("ABC_123@?");
        user2.setPassword("@?ab");

        when(authRepository.createUser(user)).thenReturn(true);
        when(authRepository.createUser(user2)).thenReturn(true);
        assertEquals(authService.createUser(user), true);
        assertEquals(authService.createUser(user2), true);
    }

    @Test
    void createUser_emptyPassword_throwsException() {
        User user = new User();
        user.setUsername("name123");
        user.setPassword("");

        assertThrows(IllegalArgumentException.class,
                () -> authService.createUser(user)
        );

        verifyNoInteractions(authRepository);
    }

    @Test
    void createUser_passwordNone_throwsException() {
        User user = new User();
        user.setUsername("name123");

        assertThrows(IllegalArgumentException.class,
                () -> authService.createUser(user)
        );

        verifyNoInteractions(authRepository);
    }

    @Test
    void createUser_emptyUsername_throwsException() {
        User user = new User();
        user.setUsername("");
        user.setPassword("pw_123");

        assertThrows(IllegalArgumentException.class,
                () -> authService.createUser(user)
        );

        verifyNoInteractions(authRepository);
    }

    @Test
    void createUser_usernameNone_throwsException() {
        User user = new User();
        user.setPassword("pw_123");

        assertThrows(IllegalArgumentException.class,
                () -> authService.createUser(user)
        );

        verifyNoInteractions(authRepository);
    }

    @Test
    public void createUser_noParams_throwsException() {
        User user = new User();

        assertThrows(IllegalArgumentException.class,
                () -> authService.createUser(user)
        );

        verifyNoInteractions(authRepository);
    }

    @Test
    public void logIn_emptyUsername_throwsException(){
        User user = new User();
        user.setPassword("pw123");
        user.setUsername("");

        assertThrows(IllegalArgumentException.class,
                () -> authService.logIn(user)
        );

        verifyNoInteractions(authRepository);
    }

    @Test
    public void logIn_emptyPassword_throwsException(){
        User user = new User();
        user.setPassword("pw123");
        user.setUsername("");

        assertThrows(IllegalArgumentException.class,
                () -> authService.logIn(user)
        );

        verifyNoInteractions(authRepository);
    }

    @Test
    public void logIn_emptyAll_throwsException(){
        User user = new User();
        user.setPassword("pw123");
        user.setUsername("");

        assertThrows(IllegalArgumentException.class,
                () -> authService.logIn(user)
        );

        verifyNoInteractions(authRepository);
    }

    @Test
    public void getUserIdTest_success(){
        User user = new User();
        user.setPassword("pw123");
        user.setUsername("name123");

        when(authRepository.getUserId("name123")).thenReturn(1);

        assertEquals(authService.getUserId(user.getUsername()),1);
    }

    /*
    @Test
    public void tokenExistsTest() {
        String s = "Bearer user1-mrpToken";
        String b = "";

        TokenStore tokenStore = mock(TokenStore.class);
        when(tokenStore.tokenExists(s)).thenReturn(true);

        assertEquals(authService.tokenExists(b,false),true);
    }
     */
}
