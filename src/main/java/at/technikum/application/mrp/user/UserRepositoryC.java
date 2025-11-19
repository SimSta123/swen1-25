package at.technikum.application.mrp.user;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.mrp.media.Media;
import at.technikum.application.todo.model.Todo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryC implements UserRepository {

    private final ConnectionPool connectionPool;

    private static final String CREATE
            = "INSERT INTO users (username, password, uuid) VALUES (?,?,?)";

    private static final String SELECT_BY_ID
            = "SELECT * FROM users WHERE id = ?";

    private static final String SELECT_ALL
            = "SELECT * FROM users";

    private static final String UPDATE_BY_ID
            = "UPDATE users SET username = ?, password = ? WHERE id = ?";

    //User muss nicht eig Profil löschen können
    private final String DELETE_BY_ID
            = "DELETE FROM users WHERE id=?";

    public UserRepositoryC(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Optional<User> find(String id) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID);
        ) {
            System.out.println("trying to read user");
            pstmt.setInt(1, Integer.parseInt(id));

            //try (ResultSet rs = pstmt.getResultSet()) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {return Optional.empty();
                }

                User user = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        Integer.parseInt(rs.getString("id"))
                        );

                return Optional.of(user);
            }

        } catch (SQLException e) {

            throw new RuntimeException(e);
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


}
