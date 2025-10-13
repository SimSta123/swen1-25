package at.technikum.application.mrp.media;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MediaRepositoryC implements MediaRepository {

    @Override
    public Optional<Media> find(String id) {
        return Optional.empty();
    }

    @Override
    public List<Media> findAll() {
        Media media = new Media();
        media.setTitle("1");
        List<Media> medias = new ArrayList<>();
        medias.add(media);
        return medias;
    }

    @Override
    public Media save(Media media) {
        return media;
    }

    @Override
    public Media delete(String id) {
        return null;
    }
}
