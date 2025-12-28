package at.technikum.application.mrp.user;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.mrp.media.Media;
import at.technikum.application.mrp.rating.Rating;

import javax.sound.midi.SysexMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryC implements UserRepository {

    private final ConnectionPool connectionPool;

    private String SELECT_BY_ID
            = "SELECT * FROM users WHERE id = ?";

    private final String SELECT_ALL
            = "SELECT * FROM users";

    private final String UPDATE_BY_ID
            = "UPDATE users SET username = ?, password = ? WHERE id = ?";

    //User muss nicht eig Profil löschen können
    private final String DELETE_BY_ID
            = "DELETE FROM users WHERE id=?";

    private final String FIND_BY_USER_ID
            = "SELECT * FROM ratings WHERE userId = ? ORDER BY created_at DESC";

    private final String FAVS_BY_USER_ID
            = "SELECT mediaId FROM favorites WHERE userId = ?";

    private final String GET_ALL_MEDIA_WHERE
            = "SELECT * FROM media WHERE mediaID = ?";

    private final String GET_GENRES_ID
            = "SELECT genreId FROM media_genres WHERE mediaId = ?";

    private final String GET_GENRE_NAME
            = "SELECT genreName FROM genres WHERE id = ?";

    public UserRepositoryC(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Optional<User> find(int id) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID);
        ) {
            System.out.println("trying read users");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                System.out.println(id);
                return Optional.empty();
            }

            User user = new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    Integer.parseInt(rs.getString("id"))
            );
            return Optional.of(user);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user with id \"" + id,e);
        }
    }

    @Override
    public List<User> findAll() {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL);
        ) {
            System.out.println("trying to read all user");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return List.of();
                }
                List<User> users = new ArrayList<>();
                while(rs.next()) {
                    User user = new User(
                            rs.getString("username"),
                            rs.getString("password"),
                            Integer.parseInt(rs.getString("id"))
                    );
                    users.add(user);
                }

                return users;
            }

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> save(User user) {
        return null;
    }

    @Override
    public User delete(String id) {
        try (
                Connection conn = connectionPool.getConnection();
        ) {
            System.out.println("trying to delete media");
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_BY_ID)) {
                pstmt.setInt(1, Integer.parseInt(id));
                pstmt.executeUpdate();
                System.out.println("media deleted");
                User user = new User();
                return user;
            }

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public User update(User user) {
        try (
                Connection conn = connectionPool.getConnection();
        ) {
            System.out.println("trying to save user");
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_BY_ID)) {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword());
                pstmt.setInt(3, user.getId());
                pstmt.executeUpdate();
                System.out.println("Database Saved");
                return user;
            }

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    public List<Rating> ratingHistory(int userId) {
        List<Rating> ratingList = new ArrayList<>();
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(FIND_BY_USER_ID);
        ) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            //nicht geordnet, da keine Ordnung muss
            while (rs.next()) {
                Rating rating = new Rating();
                rating.setId(rs.getInt("id"));
                rating.setCreatorId(rs.getInt("userId"));
                rating.setMediaId(rs.getInt("mediaId"));
                rating.setStars(rs.getInt("rating"));
                rating.setComment(rs.getString("comment"));
                rating.setTimeStamp(rs.getTimestamp("created_at").toString());
                rating.setConfirmed(rs.getBoolean("commentconfirmed"));
                System.out.println(rating.toString());
                ratingList.add(rating);
            }
            return ratingList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<Integer> allFavsMediaId(int userId){
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(FAVS_BY_USER_ID);
                ){
            System.out.println("A");
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            List<Integer> mIds = new ArrayList<>();
            while (rs.next()) {
                System.out.println("B");
                mIds.add(rs.getInt("mediaId"));
                System.out.println("C");
            }
            return mIds;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public Media findMediaByID(int mediaId) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(GET_ALL_MEDIA_WHERE);
                PreparedStatement pstmt_2 = conn.prepareStatement(GET_GENRES_ID);
                PreparedStatement pstmt_3 = conn.prepareStatement(GET_GENRE_NAME)
        ) {
            System.out.println("D");
            pstmt.setInt(1, mediaId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Media media = new Media(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("mediaType"),
                        Integer.parseInt(rs.getString("releaseYear")),
                        Integer.parseInt(rs.getString("ageRestriction")),
                        Integer.parseInt(rs.getString("creator_id")),
                        Integer.parseInt(rs.getString("mediaID"))
                );

                System.out.println("E");
                pstmt_2.setInt(1, mediaId);
                ResultSet mediaGenres = pstmt_2.executeQuery();
                List<String> genres = new ArrayList<>();
                while(mediaGenres.next()){
                    pstmt_3.setInt(1, mediaGenres.getInt("genreId"));
                    ResultSet genreNames = pstmt_3.executeQuery();
                    //genres.add(genreNames.getString("genreName"));
                    if (genreNames.next()) {
                        genres.add(genreNames.getString("genreName"));
                    }
                    genreNames.close();
                }
                media.setGenre(genres);
                return media;
            }
            return null;


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
