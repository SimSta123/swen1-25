package at.technikum.application.mrp.user;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.mrp.rating.Rating;

import javax.sound.midi.SysexMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryC implements UserRepository {

    private final ConnectionPool connectionPool;

    /*
    private static final String CREATE
            = "INSERT INTO users (username, password, uuid) VALUES (?,?,?)";
     */

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
        /*
        try (
                Connection conn = connectionPool.getConnection();
        ) {
            System.out.println("trying to save user");
            try (PreparedStatement pstmt = conn.prepareStatement(CREATE)) {
                //pstmt.setInt(1, user.getId());
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getUUId());
                pstmt.executeUpdate();
                System.out.println("Database Saved");
                return Optional.of(user);
            }

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
         */
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
                rating.setTimeStamp(rs.getTimestamp("created_at"));
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
}
