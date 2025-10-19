package at.technikum.application.mrp.leaderboard;

import at.technikum.application.common.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LeaderboardRepositoryC implements LeaderboardRepository {

    @Override
    public Optional<Leaderboard> find(String object) {
        return Optional.empty();
    }

    @Override
    public List<Leaderboard> findAll() {
        return List.of();
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