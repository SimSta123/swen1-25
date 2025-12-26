package at.technikum.application.mrp.rating;

import java.util.List;
import java.util.Optional;

public interface RatingRepository {

    Optional<Rating> find(String id);

    List<Rating> findAll();

    boolean save(Rating user, int userId);

    // Todo update(Todo todo);

    Rating delete(String id);
}
