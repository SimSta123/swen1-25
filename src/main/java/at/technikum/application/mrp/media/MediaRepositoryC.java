package at.technikum.application.mrp.media;

import at.technikum.application.common.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MediaRepositoryC implements MediaRepository {

    private final ConnectionPool connectionPool;

    private final String CREATE
            = "INSERT INTO media (title, description, mediaType, releaseYear, ageRestriction) VALUES (?,?,?,?,?)";

    private final String DELETE_BY_ID
            = "DELETE FROM media WHERE id=?";

    public MediaRepositoryC(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


    @Override
    public Optional<Media> find(String id) {
        return Optional.empty();
    }

    @Override
    public List<Media> findAll() {
        Media media = new Media();
        media.setTitle("1");
        List<Media> medias = new ArrayList<>();
        medias.add(media);
        return medias;
    }

    @Override
    public Media save(Media media) {
        try (
                Connection conn = connectionPool.getConnection();
        ) {
            System.out.println("trying to save user");
            try (PreparedStatement pstmt = conn.prepareStatement(CREATE)) {
                pstmt.setString(1, media.getTitle());
                pstmt.setString(2, media.getDescription());
                pstmt.setString(3, media.getMediaType());
                pstmt.setInt(4, media.getReleaseYear());
                pstmt.setInt(5, media.getAgeRestriction());
                pstmt.executeUpdate();
                System.out.println("Database Saved");
                return media;
            }

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public Media delete(String id) {
        try (
                Connection conn = connectionPool.getConnection();
        ) {
            System.out.println("trying to delete media");
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_BY_ID)) {
                pstmt.setString(1, id);
                pstmt.executeUpdate();
                System.out.println("media deleted");
                Media media = new Media();
                return media;
            }

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }
}
