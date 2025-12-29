package at.technikum.application.mrp.leaderboard;

import at.technikum.application.mrp.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;     //Um bestimmte sachen aus user im ObjectMapper ignorieren zu können
import com.fasterxml.jackson.annotation.JsonProperty;   //Um bestimmte sachen aus user im ObjectMapper auszugeben
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {
    public User user;
    public int ratingAnzahl = 0;

    public Leaderboard(){}
    public Leaderboard(User user, int ratingAnzahl){
        this.user = user;
        this.ratingAnzahl = ratingAnzahl;
    }

    public void setUser(User user){
        this.user = user;
    }
    @JsonIgnore
    public User getUser(){
        return this.user;
    }

    public void setRatingAnzahl(int ratingAnzahl){
        this.ratingAnzahl = ratingAnzahl;
    }

    public int getRatingAnzahl(){
        return this.ratingAnzahl;
    }

    @JsonProperty("userId")
    public int getUserId() {
        return user.getId();
    }
}
