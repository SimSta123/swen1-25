package at.technikum.application.mrp.rating;

import at.technikum.application.mrp.media.Media;

import java.sql.Timestamp;
import java.util.List;

public class Rating {
    private int id;
    //private Media media;    //ist nötig? glaube nicht
    private int mediaId;
    private int stars;
    private String comment;
    private Timestamp timeStamp;
    private List<Integer> like;
    private int creatorID;
    private boolean confirmed = false;

    public Rating() {}

    public void setId(int id) { this.id = id; }

    public int getId() { return id; }
    /*
    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

     */

    public int getStars() {return stars;}

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<Integer> getLike() {
        return like;
    }

    public void setLike(List<Integer> like) {
        like.addAll(like);
    }

    public int getCreatorId() {
        return creatorID;
    }

    public void setCreatorId(int creator) {
        this.creatorID = creator;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public void setMediaId(int mediaId) {this.mediaId = mediaId;}

    public int getMediaId() {return mediaId;}
}
