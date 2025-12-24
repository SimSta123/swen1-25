package at.technikum.application.mrp.media;

import java.util.Optional;

public interface MediaRepository {

    Optional<Media> find(String id);

    Optional<Object> findAll();

    Media save(Media media);

    // Todo update(Todo todo);

    Media delete(String id);
}
