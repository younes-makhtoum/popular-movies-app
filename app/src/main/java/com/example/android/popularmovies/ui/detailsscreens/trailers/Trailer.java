package com.example.android.popularmovies.ui.detailsscreens.trailers;

public class Trailer {

    // ID of the trailer video
    private String id;

    // YouTube key of the trailer video
    private String key;

    // Name of the trailer video
    private String name;

    /*
    * Create a new Trailer object.
    *
    * @param id is the id of the trailer video, that helps identify a single movie in the remote database
    * @param key is the YouTube key of the trailer video
    * @param name is the name of trailer video
    * */
    public Trailer(String id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    // Getter methods for the movie objects parameters
    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() { return name; }
}