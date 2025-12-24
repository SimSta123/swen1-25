package TheorIFragen;

import at.technikum.application.mrp.user.*;
import at.technikum.application.mrp.authentification.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    //@Mock
    //private UserRepository userRepository;  // Erstellt ein Mock-Objekt :contentReference[oaicite:0]{index=0}

    //@InjectMocks
    //private UserService userService;        // Injiziert das Mock in die Service-Klasse :contentReference[oaicite:1]{index=1}

    @Test
    void testCreateUser_ValidUser() {
        // Fake User list
        //List<User> users = new ArrayList<>();
        // Arrange (Given)
        UserService service = new UserService();

        // Testdaten
        User u = new User("max","123",1);
        //User u2 = new User("klaus","456",2);
        //User nextU = new User("karl","456",3);

        User created;
        //User created2;
        //User created3;
        // Act
        System.out.println("u:");
        created = service.create(u);

        //System.out.println("u2:");
        //created2 = service.create(u2);

        //System.out.println("nextU:");
        //created3 = service.create(nextU);

        //fail("Expected IllegalArgumentException but none was thrown");

        System.out.println("Users done");

        // Assert
        assertEquals(created, service.findByID(1));
        //assertEquals(created2, service.findByID(2));
        //assertEquals(created3, service.findByID(3));
        assertNotNull(created.getUUId());
        //assertNotNull(created2.getUUId());
        //assertNotNull(created3.getUUId());
        assertTrue(created.isDone());
        //assertTrue(created2.isDone());
        //assertTrue(created3.isDone());
    }
    /*
    @Test
    void testCreateUser_InvalidData_ThrowsException() {
        UserService service = new UserService();    //Wegen Unit-Test

        User u = new User("x"," ",1);
        User u2 = new User("","y",2);

        // Static Mock: checkData gibt false zurück
        try {
            // Assert
           service.create(u);
           System.out.println("u done");
           service.create(u2);
            System.out.println("u2 done");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: "+e.getMessage());
        }
    }
    */

    //Unit Test
    @Test
    void testFindUserById() {
        UserService service = new UserService();
        UserService mockService = mock(UserService.class);

        User u = new User("max","123",1);
        User u2 = new User("klaus","456",2);
        User u3 = new User("karl","456",3);
        //mockService.create(u);
        service.create(u);
        //mockService.create(u2);
        service.create(u2);
        //mockService.create(u3);
        service.create(u3);
        //assertEquals(u,mockService.findByID(1));
        assertEquals(u,service.findByID(1));
        //assertEquals(u2,mockService.findByID(2));
        assertEquals(u2,service.findByID(2));
        //assertEquals(u3,mockService.findByID(3));
        assertEquals(u3,service.findByID(3));
    }


}


