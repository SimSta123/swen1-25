package at.technikum.application.mrp.rating;

import at.technikum.application.common.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RatingRepositoryC implements RatingRepository{

    private final ConnectionPool connectionPool;

    private final String CREATE_RATING
            = "INSERT INTO ratings (userId, mediaID, rating, comment, commentconfirmed) VALUES (?,?,?,?,?)";

    public RatingRepositoryC (ConnectionPool connectionPool) {this.connectionPool = connectionPool;}

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
    public boolean save(Rating rating, int mediaId) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(CREATE_RATING);
        ) {
            pstmt.setInt(1, rating.getCreatorId());
            pstmt.setInt(2, mediaId);
            pstmt.setInt(3, rating.getStars());
            pstmt.setString(4, rating.getComment());
            pstmt.setBoolean(5,rating.isConfirmed());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Rating delete(String id) {
        return null;
    }
}
