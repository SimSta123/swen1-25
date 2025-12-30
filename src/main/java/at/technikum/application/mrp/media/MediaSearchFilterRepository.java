package at.technikum.application.mrp.media;

import at.technikum.application.common.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MediaSearchFilterRepository implements MediaRepository{

    private final ConnectionPool connectionPool;

    private final String ALL_MEDIA_BY_TITLE
            ="SELECT * FROM media WHERE title ILIKE ?";     //lower SELECT * FROM media WHERE LOWER(title) LIKE LOWER(?) würde auch gehen

    private final String SELECT_RATING
            = "SELECT rating FROM ratings WHERE mediaId = ?";

    private final String SELECT_GENRE_ID
            = "SELECT genreId from media_genres WHERE mediaId = ?";

    private final String GET_GENRE_NAME
            = "SELECT genreName from genres WHERE mgid = ?";

    /*
    private final String GET_MEDIA
            = "SELECT m.title, m.description, m.mediaType, m.releaseYear, m.agerestriction, m.average_score, m.mediaid, m.creator_id, mg.mgid, g.genrename, g.mgid"+
                "FROM media m "+
                "JOIN media_genres mg ON mg.mediaid = m.mediaid "+
                "JOIN genres g ON g.id = mg.genreid "+
                "WHERE m.title = ?";
     */
    private final String GET_MEDIA =
            "SELECT m.title, m.description, m.mediaType, m.releaseYear, m.agerestriction, " +
                    "m.average_score, m.mediaid, m.creator_id, g.mgid AS media_genre_id, g.genrename, g.mgid AS genre_id " +
                    "FROM media m " +
                    "JOIN media_genres mg ON mg.mediaid = m.mediaid " +
                    "JOIN genres g ON g.mgid = mg.genreid " +
                    "WHERE m.title ILIKE ?";


    public MediaSearchFilterRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Optional<Media> find(int id) {
        return Optional.empty();
    }

    @Override
    public List<Media> findAll() {
        return List.of();
    }

    @Override
    public Media save(Media media) {
        return null;
    }

    @Override
    public Media delete(int id) {
        return null;
    }
    /*
    public List<Media> findAll(String title) {
        System.out.println("Title:"+title);
        List<Media> medias = new ArrayList<>();
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(ALL_MEDIA_BY_TITLE);
                PreparedStatement pstmt_2 = conn.prepareStatement(SELECT_RATING);
                PreparedStatement pstmt_3 = conn.prepareStatement(SELECT_GENRE_ID);
                PreparedStatement pstmt_4 = conn.prepareStatement(GET_GENRE_NAME);
        ) {
            pstmt.setString(1, "%" + title + "%");  //die % machen das davor sowie danach beliebig sien kann

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Media media = new Media(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("mediaType"),
                        Integer.parseInt(rs.getString("releaseYear")),
                        Integer.parseInt(rs.getString("ageRestriction")),
                        Integer.parseInt(rs.getString("creator_id")),
                        Integer.parseInt(rs.getString("mediaID"))
                );
                pstmt_2.setInt(1,media.getMediaID());
                ResultSet rs_2 = pstmt_2.executeQuery();
                double count = 0;
                int rating = 0;
                while(rs_2.next()){
                    //media.setAverageRating(rs.getInt("rating"));
                    rating += rs_2.getInt("rating");
                    System.out.println("score:" +rating+" count: "+count);
                    count++;
                }
                if(count>0&&rating>0){
                    media.setAverageRating(media.getAverageRating()/count);
                    media.setAverageRating(rating/count);
                }
                pstmt_3.setInt(1, media.getMediaID());
                ResultSet genreId = pstmt_3.executeQuery();
                List<String> ls = new ArrayList<>();
                while(genreId.next()) {
                    pstmt_4.setInt(1, genreId.getInt("genreId"));
                    ResultSet genreName = pstmt_4.executeQuery();
                    while (genreName.next()) {
                        ls.add(genreName.getString("genreName"));
                        System.out.println("Genre: "+genreName.getString("genreName"));
                    }
                }
                media.setGenre(ls);
                ls.clear();
                //System.out.println(media.toString());
                System.out.println(media.getGenre().toString());
                medias.add(media);
            }
            return medias;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
     */

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
                if(found==false) {
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

    public List<Media> getGenres(List<Media> medias){
        System.out.println("1");
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_GENRE_ID);
                PreparedStatement pstmt_2 = conn.prepareStatement(GET_GENRE_NAME)
        ) {
            System.out.println("2");
            for(int i = 0; i<medias.size(); i++) {
                pstmt.setInt(1, medias.get(i).getMediaID());
                ResultSet mediaGenres = pstmt.executeQuery();
                List<String> genres = new ArrayList<>();
                System.out.println("3+"+i);
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
}
