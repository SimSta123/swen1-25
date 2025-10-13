package at.technikum.application.mrp.rating;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RatingRepositoryC implements RatingRepository{

    @Override
    public Optional<Rating> find(String id) {
        return Optional.empty();
    }

    @Override
    public List<Rating> findAll() {
        Rating rating = new Rating();
        rating.setComment("1");
        List<Rating> ratings = new ArrayList<>();
        ratings.add(rating);
        return ratings;
    }

    @Override
    public Rating save(Rating rating) {
        return rating;
    }

    @Override
    public Rating delete(String id) {
        return null;
    }
}
