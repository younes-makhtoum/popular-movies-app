package com.example.android.popularmovies.ui.detailsscreens.reviews;

public class Review {

    // ID of the review
    private String id;

    // URL of the review
    private String url;

    // Author of the review
    private String author;

    // Content of the review
    private String content;

    /*
    * Create a new Review object.
    *
    * @param id is the id of the review, that helps identify a single movie review in the remote database
    * @param url is the url of the original review of the movie
    * @param author is the author name of the review
    * @param content is the content of the review
    * */
    public Review(String id, String url, String author, String content) {
        this.id = id;
        this.url = url;
        this.author = author;
        this.content = content;
    }

    // Getter methods for the movie objects parameters
    public String getId() { return id; }

    public String getUrl() { return url; }

    public String getAuthor() { return author; }

    public String getContent() { return content; }
}