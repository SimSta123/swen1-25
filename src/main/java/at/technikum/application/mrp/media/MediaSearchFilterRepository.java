package at.technikum.application.mrp.media;

import at.technikum.application.common.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MediaSearchFilterRepository {

    private final ConnectionPool connectionPool;

    private final String ALL_MEDIA_BY_TITLE
            = "SELECT * FROM media WHERE title ILIKE ?";     //lower SELECT * FROM media WHERE LOWER(title) LIKE LOWER(?) würde auch gehen

    private final String SELECT_RATING
            = "SELECT rating FROM ratings WHERE mediaId = ?";

    private final String SELECT_GENRE_ID
            = "SELECT genreId from media_genres WHERE mediaId = ?";

    private final String GET_GENRE_NAME
            = "SELECT genreName from genres WHERE mgid = ?";

    private final String GET_MEDIA
            = "SELECT m.title, m.description, m.mediaType, m.releaseYear, m.agerestriction, " +
            "m.average_score, m.mediaid, m.creator_id, g.mgid AS media_genre_id, g.genrename, g.mgid AS genre_id " +
            "FROM media m " +
            "JOIN media_genres mg ON mg.mediaid = m.mediaid " +
            "JOIN genres g ON g.mgid = mg.genreid " +
            "WHERE m.title ILIKE ?";

    public MediaSearchFilterRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
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
