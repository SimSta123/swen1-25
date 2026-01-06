package at.technikum.application.mrp.rating;

import at.technikum.application.todo.exception.DuplicateAlreadyExistsException;
import at.technikum.application.todo.exception.EntityNotFoundException;

import java.util.List;

public class RatingService {


    private final RatingRepositoryC ratingRepository;

    public RatingService(RatingRepositoryC ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public boolean create(Rating rating, int mediaId) {
        // is todo valid?
        //rating.setRating(rating.getRating());


        return ratingRepository.save(rating, mediaId);
    }

    public Rating get(int id) {
        return ratingRepository.find(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Rating> getAll(int id) {
        return ratingRepository.findAll(id);
    }

    public void update(Rating update, int userId) {
        if(ratingRepository.ratingExistsById(update.getId(), userId)){
            throw new EntityNotFoundException("Rating with userId: "+ userId+ ", and mediaId: "+ update.getMediaId()+ ", does not Exist");
        }

        ratingRepository.update(update, userId);
    }

    public boolean delete(int ratingId, int userId) {
        if(!ratingRepository.ratingExistsById(ratingId, userId)){
            throw new EntityNotFoundException("Rating with userId: "+ userId+ ", and mediaId: "+ ratingId+ ", does not Exist");
        }

        return ratingRepository.delete(ratingId, userId);
    }

    public boolean like(int ratingId, int userId){
        //soll hier true oder falls sein?
        if(!ratingRepository.ratingExists(ratingId)){
            throw new EntityNotFoundException("Rating with ratingId: "+ ratingId+ ", does not Exist");
        }

        return ratingRepository.like(ratingId, userId);
    }

    public boolean confirm(int ratingId, int userId){
        if(!ratingRepository.ratingExistsById(ratingId, userId)){
            throw new EntityNotFoundException("Rating with ratingId:"+ratingId+" doesnt exist");
        }

        return ratingRepository.confirm(ratingId,userId);
    }
}
