package at.technikum.application.mrp.rating;

import at.technikum.application.mrp.media.Media;

import java.util.List;

public class Rating {
    private int id;
    private Media media;
    private int stars;
    private String comment;
    private String timeStamp;
    private List<Integer> like;
    private int creatorID;
    private boolean confirmed = false;;

    public Rating() {}

    public void setId(int id) { this.id = id; }

    public int getId() { return id; }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
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
}
