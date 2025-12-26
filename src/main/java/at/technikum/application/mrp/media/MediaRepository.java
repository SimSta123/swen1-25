package at.technikum.application.mrp.media;

import java.util.List;
import java.util.Optional;

public interface MediaRepository {

    Optional<Media> find(int id);

    List<Media> findAll();

    Media save(Media media);

    // Todo update(Todo todo);

    Media delete(int id);
}
