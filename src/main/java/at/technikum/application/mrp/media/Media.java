package at.technikum.application.mrp.media;

import at.technikum.application.mrp.rating.Rating;

import java.util.List;

public class Media {

    /*
    private boolean movie;
    private boolean series;
    private boolean game;
     */
    private String title;
    private String description;
    private String mediaType;
    private int releaseYear;
    private List<String> genre;
    private int ageRestriction;
    private String creatorUUID;
    private List<Rating> rating;
    private double averageRating;
    private boolean isDone;
    private int mediaID;
    private int creatorID;
    //private List<String> type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public int getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(int ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public String getCreatorUUID() {
        return creatorUUID;
    }

    public void setCreatorUUID(String creatorUUID) {
        this.creatorUUID = creatorUUID;
    }

    public List<Rating> getRating() {
        return rating;
    }

    public void setRating(List<String> rating) {
        rating.addAll(rating);
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setMediaID(int mediaID) {
        this.mediaID = mediaID;
    }

    public int getMediaID() {
        return mediaID;
    }

    public int getCreatorID() {return creatorID;}

    public void setCreatorID(int creatorID) {
        this.creatorID = creatorID;
    }

    public Media(){}

    public Media(String title, String description, String mediaType, int releaseYear, int ageRestriction, int creatorID){
        this.title = title;
        this.description = description;
        this.mediaType = mediaType;
        this.releaseYear = releaseYear;
        this.ageRestriction = ageRestriction;
        this.creatorID = creatorID;
    }

    public Media(String title, String description, String mediaType, int releaseYear, int ageRestriction, int creatorID, int mediaID){
        this.title = title;
        this.description = description;
        this.mediaType = mediaType;
        this.releaseYear = releaseYear;
        this.ageRestriction = ageRestriction;
        this.creatorID = creatorID;
        this.mediaID = mediaID;
    }

    @Override
    public String toString() {
        return "Media{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", releaseYear=" + releaseYear +
                ", ageRestriction=" + ageRestriction +
                ", creatorID=" + creatorID +
                ", mediaID=" + mediaID +
                '}';
    }
}

