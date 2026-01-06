package at.technikum.application.mrp.rating;

import at.technikum.application.todo.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    private RatingRepositoryC ratingRepository;
    private RatingService ratingService;

    @BeforeEach
    void setUp() {
        ratingRepository = mock(RatingRepositoryC.class);

        ratingService = new RatingService(ratingRepository);
    }

    @Test
    void testLike_RatingExists() {
        int ratingId = 5;
        int userId = 2;

        when(ratingRepository.ratingExists(ratingId)).thenReturn(true);
        when(ratingRepository.like(ratingId, userId)).thenReturn(true);

        assertEquals(ratingService.like(ratingId, userId),true);

        verify(ratingRepository).ratingExists(ratingId);
        verify(ratingRepository).like(ratingId, userId);
    }

    @Test
    void testLike_RatingDoesNotExist() {
        int ratingId = 1;
        int userId = 1;

        when(ratingRepository.ratingExists(ratingId)).thenReturn(false);

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            ratingService.like(ratingId, userId);
        });

        assertEquals("Rating with ratingId: 1, does not Exist", thrown.getMessage());

        verify(ratingRepository).ratingExists(ratingId);
        verify(ratingRepository, times(0)).like(ratingId, userId);
    }

    @Test
    void testConfirm_RatingExists() {
        int ratingId = 1;
        int userId = 2;

        when(ratingRepository.ratingExistsById(ratingId, userId)).thenReturn(true);
        when(ratingRepository.confirm(ratingId, userId)).thenReturn(true);

        boolean result = ratingService.confirm(ratingId, userId);

        assertTrue(result);

        verify(ratingRepository).ratingExistsById(ratingId, userId);
        verify(ratingRepository).confirm(ratingId, userId);
    }

    @Test
    void testConfirm_RatingDoesNotExist() {
        int ratingId = 1;
        int userId = 2;

        when(ratingRepository.ratingExistsById(ratingId, userId)).thenReturn(false);

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            ratingService.confirm(ratingId, userId);
        });

        assertEquals("Rating with ratingId:1 doesnt exist", thrown.getMessage());

        verify(ratingRepository).ratingExistsById(ratingId, userId);
        verify(ratingRepository, times(0)).confirm(ratingId, userId);
    }
}
