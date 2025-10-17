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
    private int creatorUUID;
    private List<Rating> rating;
    private double averageRating;
    private boolean isDone;
    private int mediaID;
    //private List<String> type;

    /*
    public boolean isMovie() {
        return movie;
    }

    public void setMovie(boolean movie) {
        this.movie = movie;
    }

    public boolean isSeries() {
        return series;
    }

    public void setSeries(boolean series) {
        this.series = series;
    }

    public boolean isGame() {
        return game;
    }

    public void setGame(boolean game) {
        this.game = game;
    }
    */
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

    public int getCreatorUUID() {
        return creatorUUID;
    }

    public void setCreatorUUID(int creatorUUID) {
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
}
