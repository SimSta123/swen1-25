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

    public List<Rating> getAll() {
        return ratingRepository.findAll();
    }

    public void update(Rating update, int userId) {
        //System.out.println("Rating exists:" +ratingRepository.ratingExistsById(update.getId()));
        if(!ratingRepository.ratingExistsById(update.getId())){
            throw new DuplicateAlreadyExistsException("Rating with userId: "+ userId+ ", and mediaId: "+ update.getMediaId()+ ", does not Exist");
        }

        ratingRepository.update(update, userId);
    }

    public boolean delete(int ratingId, int userId) {
        System.out.println("exists:"+ratingRepository.ratingExistsById(ratingId));
        if(ratingRepository.ratingExistsById(ratingId)==false){
            throw new DuplicateAlreadyExistsException("Rating with userId: "+ userId+ ", and mediaId: "+ ratingId+ ", does not Exist");
        }
        System.out.println("here");

        return ratingRepository.delete(ratingId, userId);
    }

    public boolean like(int ratingId, int userId){
        if(!ratingRepository.ratingExists(ratingId, userId)){
            throw new DuplicateAlreadyExistsException("Rating with userId: "+ userId+ ", and mediaId: "+ ratingId+ ", does not Exist");
        }

        return ratingRepository.like(ratingId, userId);
    }

    public boolean confirm(int ratingId, int userId){
        if(!ratingRepository.ratingExists(ratingId, userId)){
            throw new DuplicateAlreadyExistsException("Rating with userId: "+ userId+ ", and mediaId: "+ ratingId+ ", does not Exist");
        }
        return ratingRepository.confirm(ratingId,userId);
    }
}
