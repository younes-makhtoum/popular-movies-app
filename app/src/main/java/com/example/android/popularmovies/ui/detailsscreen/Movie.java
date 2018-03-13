package com.example.android.popularmovies.ui.detailsscreen;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    // User rating of the movie
    private float userRating;

    // Title of the movie
    private String title;

    // Poster of the movie
    private String poster;

    // Plot synopsis of the movie
    private String plotSynopsis;

    // Release date of the movie
    private String releaseDate;

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

    // Getter methods for the movie objects variables
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

    // Write object values to parcel for storage
    public void writeToParcel(Parcel dest, int flags){
        dest.writeFloat(userRating);
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(plotSynopsis);
        dest.writeString(releaseDate);
    }

    // Constructor used for parcel
    public Movie(Parcel parcel){
        userRating = parcel.readFloat();
        title = parcel.readString();
        poster = parcel.readString();
        plotSynopsis = parcel.readString();
        releaseDate = parcel.readString();
    }

    // Creator - used when un-parceling our parcel (creating a Movie object)
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }
}
