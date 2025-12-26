package at.technikum.application.mrp.media;
import at.technikum.application.todo.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public class MediaService {

    private final MediaRepositoryC mediaRepository;

    public MediaService(MediaRepositoryC mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public Media create(Media media) {
        // is todo valid?
        System.out.println("in MediaService");
        //media.setTitle(media.getTitle()); //wieso????? woher wie wo was??

        return mediaRepository.save(media);
    }

    public Media get(int id) {
        return mediaRepository.find(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Media with ID " + id + " not found")
                );
    }

    public List<Media> getAll() {
        return mediaRepository.findAll();
    }

    public boolean update(Media update, int mediaID) {
        //Media media = mediaRepository.find(update.getMediaID())
        //        .orElseThrow(EntityNotFoundException::new);

        //media.setTitle(update.getTitle());
        //media.setDone(update.isDone());
        System.out.println("MediaUpdate, mediaID:"+mediaID+" creatorID: "+update.getCreatorID());
        boolean done = mediaRepository.update(update, mediaID);
        return true;
    }

    public Media delete(String title) {
        //return mediaRepository.delete(title);
        return null;
    }

    public boolean deleteByID(int creatorId, int mediaId) {
        //if(creatorId<1) throw new IllegalArgumentException("creatorID ungültig"); //sollte ja nach userId gehen
        System.out.println("deleteByID MediaService");
        Optional<Media> mediaOpt = mediaRepository.find(mediaId);
        //if(assertTrue(mediaOpt.isEmpty())) new EntityNotFoundException("MediaID not found");
        System.out.println("Optional<Media>->Media");
        Media media = mediaOpt.orElseThrow(
                () -> new EntityNotFoundException("MediaID not found")
        );
        System.out.println("CreatorID==CreatorID??||MediaID:" + media.getMediaID());
        if (media.getCreatorID() != creatorId) {
            System.out.println("Not the creator");
            throw new IllegalArgumentException("Not the creator");
        } else {
            mediaRepository.delete(mediaId);
            return true;
        }
    }
}