package at.technikum.application.mrp.leaderboard;

import java.util.List;
import java.util.Optional;

public interface LeaderboardRepository {

    Optional<Leaderboard> find(String Title);

    List<Leaderboard> findAll();

    Leaderboard save(Leaderboard media);

    // Todo update(Todo todo);

    Leaderboard delete(String Title);
}
