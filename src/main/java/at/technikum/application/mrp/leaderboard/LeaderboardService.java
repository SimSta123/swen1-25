package at.technikum.application.mrp.leaderboard;
import at.technikum.application.todo.exception.EntityNotFoundException;

import java.util.List;

public class LeaderboardService {

    private final LeaderboardRepositoryC leaderboardRepositoryC;

    public LeaderboardService(LeaderboardRepositoryC leaderboardRepositoryC) {
        this.leaderboardRepositoryC = leaderboardRepositoryC;
    }

    public Leaderboard create(Leaderboard leaderboard) {
        // is todo valid?
        return null;
    }

    public Leaderboard get(String id) {
        return leaderboardRepositoryC.find(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Leaderboard> getAll() {
        return leaderboardRepositoryC.findAll();
    }

    public Leaderboard update(String Title, Leaderboard update) {
        Leaderboard leaderboard = leaderboardRepositoryC.find(update.getTitle())
                .orElseThrow(EntityNotFoundException::new);

        leaderboard.setTitle(update.getTitle());
        //leaderboard.setDone(update.isDone());

        return leaderboardRepositoryC.save(leaderboard);
    }

    public Leaderboard delete(String title) {
        return leaderboardRepositoryC.delete(title);
    }
}