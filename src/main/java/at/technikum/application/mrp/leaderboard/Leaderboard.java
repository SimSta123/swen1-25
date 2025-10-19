package at.technikum.application.mrp.leaderboard;

import at.technikum.application.mrp.media.Media;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard {
    private List<Media> leaderboard;
    private String title;

    public Leaderboard() {
        leaderboard = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
