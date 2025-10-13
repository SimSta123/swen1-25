package at.technikum.application.mrp.media;
import at.technikum.application.todo.exception.EntityNotFoundException;

import java.util.List;

public class MediaService {

    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public Media create(Media media) {
        // is todo valid?
        media.setTitle(media.getTitle());

        return mediaRepository.save(media);
    }

    public Media get(String id) {
        return mediaRepository.find(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Media> getAll() {
        return mediaRepository.findAll();
    }

    public Media update(String Title, Media update) {
        Media media = mediaRepository.find(update.getTitle())
                .orElseThrow(EntityNotFoundException::new);

        media.setTitle(update.getTitle());
        media.setDone(update.isDone());

        return mediaRepository.save(media);
    }

    public Media delete(String title) {
        return mediaRepository.delete(title);
    }
}