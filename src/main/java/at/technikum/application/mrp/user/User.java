package at.technikum.application.mrp.user;

import java.util.List;

public class User {
    private String username;
    private String password;
    private String uuid;
    private int id;
    private boolean done;
    private List<String> favGenre;
    private List<String> favMedia;
    private List<String> likedEntries;

    public String getUUId() {
        return uuid;
    }

    public void setUUId(String id) {
        this.uuid = id;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) { this.password = password; }

    public String getPassword() { return password; }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setFavGenre(List<String> favGenre) {this.favGenre.addAll(favGenre);}

    public List<String> getFavGenre() {return favGenre;}

    public void setFavMedia(List<String> favMedia) {this.favMedia.addAll(favMedia);}

    public List<String> getFavMedia() {return favMedia;}

    public User(String username, String password, int id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public User(){ }

}
