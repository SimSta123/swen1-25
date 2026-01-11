package at.technikum.application.mrp.leaderboard;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.common.Repository;
import at.technikum.application.mrp.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LeaderboardRepositoryC implements LeaderboardRepository {

    private final ConnectionPool connectionPool;

    private final String GET_ALL_RATING_BY_USERID
            = "SELECT userId FROM ratings";

    private final String GET_ALL_RATINGS_ZWEI
            = "SELECT userId, COUNT(*) AS ratingAnzahl FROM ratings GROUP BY userId ORDER BY ratingAnzahl DESC";


    public LeaderboardRepositoryC(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Optional<Leaderboard> find(String object) {
        return Optional.empty();
    }

    @Override
    public List<Leaderboard> findAll() {
        /*
        System.out.println("in findAll repostirory");
        List<Leaderboard> lb = new ArrayList<>();
        try(
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(GET_ALL_RATING_BY_USERID)
                ) {
            ResultSet rs = pstmt.executeQuery();
            System.out.println("after Query");
            while (rs.next()) {
                int userId = rs.getInt("userId");
                boolean found = false;

                // prüfen, ob User schon im Leaderboard ist
                for (Leaderboard entry : lb) {
                    if (entry.getUser().getId() == userId) {
                        entry.setRatingAnzahl(entry.getRatingAnzahl() + 1);
                        found = true;
                        break;
                    }
                }

                // wenn nicht gefunden -> neuen Eintrag erstellen
                if (!found) {
                    User user = new User();
                    user.setId(userId);

                    Leaderboard newEntry = new Leaderboard(user, 1);
                    lb.add(newEntry);
                }
            }

            return lb;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
         */
        List<Leaderboard> lb = new ArrayList<>();
        try(
                Connection conn = connectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(GET_ALL_RATINGS_ZWEI)
        ) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("userId"));
                Leaderboard lb_2 = new Leaderboard(
                        user,
                        rs.getInt("ratingAnzahl")
                );
                System.out.println(user.getId());
                lb.add(lb_2);
            }
            for(int i = 0; i<lb.size(); i++){
                System.out.println(lb.get(i).getUser().getId());
            }
            return lb;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Leaderboard save(Leaderboard media) {
        return null;
    }

    @Override
    public Leaderboard delete(String Title) {
        return null;
    }
}