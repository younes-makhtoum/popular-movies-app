package com.example.android.popularmovies.ui.detailsscreen;

public class Movie {

    // User rating of the movie
    private final float userRating;

    // Title of the movie
    private final String title;

    // Poster of the movie
    private final String poster;

    // Plot synopsis of the movie
    private final String plotSynopsis;

    // Release date of the movie
    private final String releaseDate;

    /*
    * Create a new Movie object.
    *
    * @param userRating is the rating of the movie, calculated from a vote average of users
    * @param title is the title of the movie
    * @param poster is the link to the movie's poster
    * @param plotSynopsis is the plot synopsis providing a short summary of the movie
    * @param releaseDate is the release date of the movie
    * */
    public Movie(float userRating, String title, String poster, String plotSynopsis, String releaseDate) {
        this.userRating = userRating;
        this.title = title;
        this.poster = poster;
        this.plotSynopsis = plotSynopsis;
        this.releaseDate = releaseDate;
    }

    //Getter methods for the movie objects variables
    public float getUserRating() {
        return userRating;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() { return poster; }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
