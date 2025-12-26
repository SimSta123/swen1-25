package at.technikum.application.mrp.rating;

import at.technikum.application.todo.exception.EntityNotFoundException;

import java.util.List;

public class RatingService {


    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public boolean create(Rating rating, int mediaId) {
        // is todo valid?
        //rating.setRating(rating.getRating());


        return ratingRepository.save(rating, mediaId);
    }

    public Rating get(String id) {
        return ratingRepository.find(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Rating> getAll() {
        return ratingRepository.findAll();
    }

    public Rating update(String Title, Rating update) {
        //Hier noch wirklich ändern, nach was suchen hier? einzigartig??
        Rating rating = ratingRepository.find(update.getComment())
                .orElseThrow(EntityNotFoundException::new);

        //rating.setTitle(update.getTitle());
        //rating.setDone(update.isDone());

        //return ratingRepository.save(rating, 0);
        return rating;
    }

    public Rating delete(String title) {
        return ratingRepository.delete(title);
    }
}
