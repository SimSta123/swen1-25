package at.technikum.application.mrp.media;
import at.technikum.application.mrp.UrlID;
import at.technikum.application.mrp.rating.Rating;
import at.technikum.application.mrp.rating.RatingService;
import at.technikum.application.todo.exception.DuplicateAlreadyExistsException;
import at.technikum.application.todo.exception.EntityNotFoundException;
import at.technikum.application.todo.exception.NotAuthorizedException;
import at.technikum.server.http.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MediaService {

    private final MediaRepositoryC mediaRepository;
    //private final MediaSearchFilterRepository mediaSearchFilterRepository;
    /*
    public MediaService(MediaRepositoryC mediaRepository, MediaSearchFilterRepository mediaSearchFilterRepository) {
        this.mediaRepository = mediaRepository;
        this.mediaSearchFilterRepository = mediaSearchFilterRepository;
    }
     */

    public MediaService(MediaRepositoryC mediaRepository) {
        this.mediaRepository = mediaRepository;
    }


    public Media create(Media media) {
        //media.setTitle(media.getTitle()); //wieso????? woher wie wo was??
        /*  //Falls media einzigartig sein soll
        mediaRepository.find(media.getMediaID())
                .orElseThrow(() ->
                        new EntityNotFoundException("Media with ID " + mediaId + " not found")
                );
         */

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
        Media media = mediaRepository.find(mediaID)
                .orElseThrow(EntityNotFoundException::new);
        if(media.getCreatorID()!= update.getCreatorID()) throw new NotAuthorizedException("User not Creator of Media, not Authorized");

        System.out.println("MediaUpdate, mediaID:"+mediaID+" creatorID: "+update.getCreatorID());
        return mediaRepository.update(update, mediaID);
    }

    public Media delete(String title) {
        //return mediaRepository.delete(title);
        return null;
    }

    public boolean deleteByID(int creatorId, int mediaId) {
        //if(creatorId<1) throw new IllegalArgumentException("creatorID ungültig"); //sollte ja nach userId gehen
        Optional<Media> mediaOpt = mediaRepository.find(mediaId);
        //if(mediaOpt.isEmpty()) new EntityNotFoundException("MediaID not found");
        Media media = mediaOpt.orElseThrow(
                () -> new EntityNotFoundException("MediaID not found")
        );
        if (media.getCreatorID() != creatorId) {
            throw new NotAuthorizedException("Not the creator, not Authorized");
        } else {
            mediaRepository.delete(mediaId);
            return true;
        }
    }
    //Hier oder im Ratingservice?
    public boolean createRating(Rating rating, int mediaId) {

        mediaRepository.find(mediaId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Media with ID " + mediaId + " not found")
                );

        if(mediaRepository.ratingExists(mediaId, rating.getCreatorId())){
            throw new DuplicateAlreadyExistsException("Rating with the given params already exists");
        }
        System.out.println("media found and rating not found");
        return mediaRepository.saveRating(rating, mediaId);
    }

    public boolean fav(int mediaId, int userId){
        mediaRepository.find(mediaId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Media with ID " + mediaId + " not found")
                );
        System.out.println("1");

        if(mediaRepository.favExists(mediaId, userId)){
            throw new DuplicateAlreadyExistsException("Fav with the given params already exists");
        }
        System.out.println("2");

        return mediaRepository.fav(mediaId, userId);
    }

    public boolean favDelete(int mediaId, int userId){
        mediaRepository.find(mediaId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Media with ID " + mediaId + " not found")
                );

        if(!mediaRepository.favExists(mediaId, userId)){
            throw new DuplicateAlreadyExistsException("Fav with the given params doesnt exist");
        }

        return mediaRepository.favDelete(mediaId, userId);
    }

    public List<Media> search(String url){
        List<Media> medias = new ArrayList<>();
        System.out.println("kk--------------------------------------------------------");
        String title = UrlID.handleMediaTitle(url);
        //medias = mediaSearchFilterRepository.findAll(UrlID.handleMediaTitle(url));
        String genre = UrlID.handleMediaGenre(url);
        String mediaType = UrlID.handleMediaType(url);
        String relYear = UrlID.handleMediaRelYear(url);
        String ageRes = UrlID.handleMediaAgeRestr(url);
        String rating = UrlID.handleMediaRating(url);
        String sortBy = UrlID.handleSortBy(url);
        medias = mediaRepository.filter(title, genre, mediaType, relYear, ageRes, rating, sortBy);
        return mediaRepository.getGenres(medias);
    }
}