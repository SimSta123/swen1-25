package at.technikum.application.mrp.authentification;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.mrp.user.User;
import at.technikum.application.todo.exception.DuplicateAlreadyExistsException;
import at.technikum.application.todo.exception.EntityNotFoundException;
import at.technikum.server.http.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthRepositoryC {

    private final ConnectionPool connectionPool;

    private final String CREATE
            = "INSERT INTO users (username, password, uuid) VALUES (?,?,?)";

    private final String USER_LOGIN
            = "SELECT * FROM users WHERE username = ? AND password = ?";

    private final String USERNAME_EXISTS
            = "SELECT * FROM users WHERE username = ?";

    private final String GET_USERID
            = "SELECT id FROM users WHERE username = ?";

    public AuthRepositoryC(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public boolean createUser(User user){
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(CREATE);
        ) {
            System.out.println("trying to save user");
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getUUId());
            System.out.println("before pstmt eU");
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DuplicateAlreadyExistsException(e);
        }
    }

    public boolean userLogIn(User user){
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(USER_LOGIN);
        ) {
            System.out.println("trying to save user");
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            System.out.println("before pstmt eU");
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getUserId(String username) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(GET_USERID);
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("user not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
