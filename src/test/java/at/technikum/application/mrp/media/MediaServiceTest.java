package at.technikum.application.mrp.media;

import at.technikum.application.mrp.authentification.AuthRepositoryC;
import at.technikum.application.mrp.authentification.AuthService;
import at.technikum.application.todo.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MediaServiceTest {

    @Mock
    private MediaRepositoryC mediaRepository;

    private MediaService mediaService;

    @BeforeEach
    void setUp() {
        mediaService = new MediaService(mediaRepository);
    }

    @Test
    public void get_wrongId_throwException(){
        assertThrows(EntityNotFoundException.class,
                () -> mediaService.get(0)
        );
    }

    @Test
    public void update_wrongCreatorID_throException(){
        Media media = new Media("Star Wars Updated", "Updated Desc", "movie", 1995, 16, 5);

        assertThrows(EntityNotFoundException.class,
                () -> mediaService.update(media, 41)
        );
    }
}
