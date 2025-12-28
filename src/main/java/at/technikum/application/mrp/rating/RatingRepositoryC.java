package at.technikum.application.mrp.rating;

import at.technikum.application.common.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RatingRepositoryC implements RatingRepository{

    private final ConnectionPool connectionPool;

    private final String CREATE_RATING
            = "INSERT INTO ratings (userId, mediaID, rating, comment, created_at, commentconfirmed) VALUES (?,?,?,?,?)";

    private final String FIND_BY_ID
            = "SELECT * FROM ratings WHERE id = ? ";

    private final String RATING_EXISTS
            = "SELECT * from ratings WHERE userId = ? AND mediaId = ?";

    private final String RATING_EXISTS_BY_ID
            = "SELECT * from ratings WHERE id = ?";

    private final String LIKE_RATING
            = "INSERT INTO rating_likes (userId, ratingId) VALUES (?,?)";

    private final String UPDATE_RATING
            = "UPDATE ratings SET rating = ?, comment = ? WHERE id = ? AND userId = ?";

    private final String CONFIRM_RATING
            = "UPDATE ratings SET commentconfirmed = true WHERE id = ? AND userId = ?";

    private final String DELETE_RATING
            = "DELETE FROM ratings WHERE id = ? AND userId = ?";

    public RatingRepositoryC (ConnectionPool connectionPool) {this.connectionPool = connectionPool;}

    @Override
    public Optional<Rating> find(int id) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(FIND_BY_ID);
        ) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            Rating rating = new Rating();
            if(rs.next()){
                rating.setId(rs.getInt("id"));
                rating.setCreatorId(rs.getInt("userId"));
                rating.setMediaId(rs.getInt("mediaId"));
                rating.setStars(rs.getInt("rating"));
                rating.setComment(rs.getString("comment"));
                rating.setTimeStamp(rs.getTimestamp("created_at"));
                rating.setConfirmed(rs.getBoolean("commentconfirmed"));
                return Optional.of(rating);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
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
    public boolean delete(int ratingId, int userId) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(DELETE_RATING)
        ) {
            pstmt.setInt(1, ratingId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean ratingExists(int mediaId, int userId) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(RATING_EXISTS);
        ) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, mediaId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean ratingExistsById(int rId) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(RATING_EXISTS_BY_ID);
        ) {
            pstmt.setInt(1, rId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean like(int ratingId, int userid){
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(LIKE_RATING)
        ) {
            pstmt.setInt(1, userid);
            pstmt.setInt(2, ratingId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update(Rating update, int userId){
        //Geht nur wenn Stars und comment angegeben werden
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(UPDATE_RATING)
        ) {
            if(Integer.valueOf(update.getStars())!=null) {
                pstmt.setInt(1, update.getStars());
            }
            if(update.getComment()!=null) {
                pstmt.setString(2, update.getComment());
            }
            pstmt.setInt(3,update.getId());
            pstmt.setInt(4,userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean confirm(int ratingId, int userId){
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(CONFIRM_RATING)
        ) {
            pstmt.setInt(1,ratingId);
            pstmt.setInt(2,userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
