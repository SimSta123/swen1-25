package at.technikum.application.mrp.media;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.mrp.rating.Rating;
import at.technikum.application.todo.exception.EntityNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MediaRepositoryC implements MediaRepository {

    private final ConnectionPool connectionPool;

    private final String CREATE
            = "INSERT INTO media (title, description, mediaType, releaseYear, ageRestriction, creator_id) VALUES (?,?,?,?,?,?)";

    private final String CREATE_GENRES
            = "INSERT INTO genres (genreName) VALUES (?) ON CONFLICT (genreName) DO NOTHING";   //mehrere gleiche GenreName können eingegeben werden, nicht passiert

    private final String CREATE_GENRES_ASSOCIATED
            = "INSERT INTO media_genres (mediaID,genreID) VALUES (?,?)";

    private final String DELETE_BY_ID
            = "DELETE FROM media WHERE mediaID=?";

    private final String GET_ALL
            = "SELECT * FROM media";

    private final String GET_GENRE_ID
            = "SELECT * FROM genres WHERE genreName=?";

    private final String GET_MEDIA_ID
            = "SELECT * FROM media WHERE title=?";

    private final String GET_MEDIA_WHERE_ID
            = "SELECT * FROM media WHERE mediaID=?";

    private final String UPDATE_MEDIA
            = "UPDATE media SET title = ?, description = ?, mediaType = ?, releaseYear = ?, ageRestriction = ? WHERE mediaID = ? AND creator_id = ?";

    private final String CREATE_RATING
            = "INSERT INTO ratings (userId, mediaID, rating, comment, commentconfirmed) VALUES (?,?,?,?,?)";

    private final String RATING_EXISTS
            = "SELECT * from ratings WHERE mediaId = ? AND userId = ?";

    private final String ALL_RATINGS
            = "SELECT rating FROM ratings WHERE mediaId = ?";

    private final String FAV
            = "INSERT INTO favorites (mediaId, userId) VALUES (?,?)";

    private final String FAV_DELETE
            = "DELETE FROM favorites WHERE mediaId = ? AND userId = ?";

    private final String FAV_EXISTST
            = "SELECT * FROM favorites WHERE mediaId = ? AND userId = ?";

    private final String GET_GENRE_NAME
            = "SELECT genreName from genres WHERE mgid = ?";

    private final String ALL_MEDIA_BY_TITLE
            = "SELECT * FROM media WHERE title ILIKE ?";     //lower SELECT * FROM media WHERE LOWER(title) LIKE LOWER(?) würde auch gehen

    private final String SELECT_RATING
            = "SELECT rating FROM ratings WHERE mediaId = ?";

    private final String SELECT_GENRE_ID
            = "SELECT genreId from media_genres WHERE mediaId = ?";

    //private final String GET_GENRE_NAME
    //        = "SELECT genreName from genres WHERE mgid = ?";

    private final String GET_MEDIA
            = "SELECT m.title, m.description, m.mediaType, m.releaseYear, m.agerestriction, " +
            "m.average_score, m.mediaid, m.creator_id, g.mgid AS media_genre_id, g.genrename, g.mgid AS genre_id " +
            "FROM media m " +
            "JOIN media_genres mg ON mg.mediaid = m.mediaid " +
            "JOIN genres g ON g.mgid = mg.genreid " +
            "WHERE m.title ILIKE ?";

    public MediaRepositoryC(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Optional<Media> find(int id) {
        System.out.println("media_find:"+id);
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(GET_MEDIA_WHERE_ID);
        ) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Media media = new Media(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("mediaType"),
                        rs.getInt("releaseYear"),
                        rs.getInt("ageRestriction"),
                        rs.getInt("creator_id"),
                        rs.getInt("mediaID"),
                        rs.getDouble("average_score")
                );
                return Optional.of(media);
            } else {
                return Optional.empty(); // nichts gefunden
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Media> findAll() {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(GET_ALL);
        ) {
            List<Media> medias = new ArrayList<>();
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("in while");
                Media media = new Media(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("mediaType"),
                        rs.getInt("releaseYear"),
                        rs.getInt("ageRestriction"),
                        rs.getInt("creator_id"),
                        rs.getInt("mediaID"),
                        rs.getInt("average_score")
                );
                medias.add(media);
            }
            return medias;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Media save(Media media) {
        try (
                Connection conn = connectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CREATE);
             PreparedStatement pstmt_2 = conn.prepareStatement(CREATE_GENRES);
             PreparedStatement pstmt_3 = conn.prepareStatement(CREATE_GENRES_ASSOCIATED);
             PreparedStatement pstmt_GENREID = conn.prepareStatement(GET_GENRE_ID);
             PreparedStatement pstmt_MEDIAID = conn.prepareStatement(GET_MEDIA_ID);
             ) {
            System.out.println("trying to save media");
            pstmt.setString(1, media.getTitle());
            pstmt.setString(2, media.getDescription());
            pstmt.setString(3, media.getMediaType());
            pstmt.setInt(4, media.getReleaseYear());
            pstmt.setInt(5, media.getAgeRestriction());
            pstmt.setInt(6, media.getCreatorID());
            System.out.println("before pstmt uq");
            pstmt.executeUpdate();
            System.out.println("after");
            System.out.println("--------pstmt ausgeführt---------");

            pstmt_MEDIAID.setString(1, media.getTitle());
            int mediaID = 0;
            ResultSet rs = pstmt_MEDIAID.executeQuery();
            if (rs.next()) {
                mediaID = rs.getInt("mediaID");
            }

            String[] genres = media.getGenre().toArray(new String[0]);
            for(int i = 0; i < genres.length; i++) {
                pstmt_2.setString(1,genres[i]);
                pstmt_2.executeUpdate();

                System.out.println("--------pstmt_2 ausgeführt---------");

                pstmt_GENREID.setString(1, genres[i]);
                int genreID = 0;
                rs = pstmt_GENREID.executeQuery();
                if (rs.next()) {
                    genreID = rs.getInt("mgid");
                }

                pstmt_3.setInt(1,mediaID);
                pstmt_3.setInt(2,genreID);
                pstmt_3.executeUpdate();

                System.out.println("--------pstmt_3 ausgeführt---------");

                System.out.println("Database Saved");
            }
            return media;

        } catch (SQLException e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<String> getGenreName(int mediaId){
        List<String> genres = new ArrayList<>();
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(GET_GENRE_NAME);
        ){
            pstmt.setInt(1, mediaId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                genres.add(rs.getString("genreName"));
            }
            return genres;
        } catch (SQLException e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Media delete(int id) {
        try (
                Connection conn = connectionPool.getConnection();
        ) {
            System.out.println("trying to delete media");
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_BY_ID)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                System.out.println("media deleted");
                Media media = new Media();
                return media;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean update(Media media, int id){
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(UPDATE_MEDIA);
                )
        {
            pstmt.setString(1, media.getTitle());
            pstmt.setString(2, media.getDescription());
            pstmt.setString(3, media.getMediaType());
            pstmt.setInt(4, media.getReleaseYear());
            pstmt.setInt(5, media.getAgeRestriction());
            pstmt.setInt(6, id);
            pstmt.setInt(7, media.getCreatorID()); //soll dann mal UserID werden

            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new EntityNotFoundException("Media not found or not creator");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean saveRating(Rating rating, int mediaId) {
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
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean ratingExists(int mediaId, int userId) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(RATING_EXISTS);
        ) {
            pstmt.setInt(1, mediaId);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean fav(int mediaId, int userId){
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(FAV);
        ) {
            pstmt.setInt(1, mediaId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean favDelete(int mediaId, int userId){
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(FAV_DELETE);
        ) {
            pstmt.setInt(1, mediaId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean favExists(int mediaId, int userId){
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(FAV_EXISTST);
        ) {
            pstmt.setInt(1, mediaId);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Media> findAll(String title) {
        System.out.println("Title:" + title);
        List<Media> medias = new ArrayList<>();
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(GET_MEDIA)
                //PreparedStatement pstmt = conn.prepareStatement(ALL_MEDIA_BY_TITLE)
        ) {
            pstmt.setString(1, "%" + title + "%");  //die % machen das davor sowie danach beliebig sien kann
            ResultSet rs = pstmt.executeQuery();
            boolean found = false;
            while (rs.next()) {
                for (Media m : medias) {
                    if (m.getMediaID() == Integer.parseInt(rs.getString("mediaId"))) {
                        found = true;
                        break;
                    }
                }
                if (found == false) {
                    Media media = new Media();
                    media.setTitle(rs.getString("title"));
                    media.setDescription(rs.getString("description"));
                    media.setMediaType(rs.getString("mediaType"));
                    media.setReleaseYear(Integer.parseInt(rs.getString("releaseYear")));
                    media.setAgeRestriction(Integer.parseInt(rs.getString("ageRestriction")));
                    media.setCreatorID(Integer.parseInt(rs.getString("creator_id")));
                    media.setMediaID(Integer.parseInt(rs.getString("mediaID")));
                    media.setAverageRating(Double.parseDouble(rs.getString("average_score")));
                    System.out.println(media.toString());
                    medias.add(media);
                }
                found = false;
            }
            return medias;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Media> getGenres(List<Media> medias) {
        System.out.println("1");
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_GENRE_ID);
                PreparedStatement pstmt_2 = conn.prepareStatement(GET_GENRE_NAME)
        ) {
            System.out.println("2");
            for (int i = 0; i < medias.size(); i++) {
                pstmt.setInt(1, medias.get(i).getMediaID());
                ResultSet mediaGenres = pstmt.executeQuery();
                List<String> genres = new ArrayList<>();
                System.out.println("3+" + i);
                while (mediaGenres.next()) {
                    pstmt_2.setInt(1, mediaGenres.getInt("genreId"));
                    ResultSet genreNames = pstmt_2.executeQuery();
                    //genres.add(genreNames.getString("genreName"));
                    if (genreNames.next()) {
                        genres.add(genreNames.getString("genreName"));
                    }
                    genreNames.close();
                }
                medias.get(i).setGenre(genres);
            }
            return medias;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Media> filter(String title, String genre, String mediaType, String relYear, String ageRes, String rating, String sortBy) {
        try (
                Connection conn = connectionPool.getConnection();
        ) {
            List<Object> params = new ArrayList<>();

            //StringBuilder für dynmaische QUeryies
            StringBuilder filter = new StringBuilder("""       
                SELECT DISTINCT m.*
                FROM media m
                LEFT JOIN media_genres mg ON mg.mediaid = m.mediaid
                LEFT JOIN genres g ON g.mgid = mg.genreid
                WHERE 1=1
            """);

            if (title != null) {
                filter.append(" AND m.title ILIKE ?");
                params.add("%" + title + "%");
            }
            if (genre != null) {
                filter.append(" AND g.genrename = ?");
                params.add(genre);
            }
            if (mediaType != null) {
                filter.append(" AND m.mediatype = ?");
                params.add(mediaType);
            }
            if (relYear != null) {
                filter.append(" AND m.releaseyear = ?");
                params.add(Integer.parseInt(relYear));
            }
            if (ageRes != null) {
                filter.append(" AND m.agerestriction <= ?");
                params.add(Integer.parseInt(ageRes));
            }
            if (rating != null) {   //min rating
                filter.append(" AND m.average_score >= ?");
                params.add(Double.valueOf(Double.parseDouble(rating)));
            }
            if (sortBy != null) {
                filter.append(" ORDER BY ");
                switch (sortBy) {
                    case "title":
                        filter.append("m.title");
                        break;
                    case "rating":
                        filter.append("m.average_score");
                        break;
                    case "releaseYear":
                        filter.append("m.releaseYear");
                        break;
                    case "ageRestriction":
                        filter.append("m.ageRestriction");
                        break;
                    default:
                        filter.append("m.title");
                }
                filter.append(" DESC");
            }

            PreparedStatement pstmt = conn.prepareStatement(filter.toString());

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pstmt.executeQuery();

            List<Media> result = new ArrayList<>();

            while (rs.next()) {
                Media media = new Media(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("mediatype"),
                        rs.getInt("releaseyear"),
                        rs.getInt("agerestriction"),
                        rs.getInt("creator_id"),
                        rs.getInt("mediaid"),
                        rs.getInt("average_score")
                );
                System.out.println(media.toString());
                result.add(media);
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
