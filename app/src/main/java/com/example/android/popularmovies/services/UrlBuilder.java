package com.example.android.popularmovies.services;

import android.net.Uri;

public class UrlBuilder {

    // Tag for log messages
    public static final String LOG_TAG = UrlBuilder.class.getName();

    private static final String SCHEME = "https";
    private static final String SITE_AUTHORITY = "www.themoviedb.org";
    private static final String API_AUTHORITY = "api.themoviedb.org";
    private static final String IMAGE_AUTHORITY = "image.tmdb.org";
    private static final String YOUTUBE_IMAGE_AUTHORITY = "img.youtube.com";
    private static final String PRE_PATH_MOVIE = "movie";
    private static final String PRE_PATH_VIDEO = "videos";
    private static final String PRE_PATH_REVIEW = "reviews";
    private static final String PRE_PATH_VI = "vi";
    private static final String PRE_PATH_3 = "3";
    private static final String PRE_PATH_T = "t";
    private static final String PRE_PATH_P = "p";
    private static final String JPG = "0.jpg";
    private static final String THUMBNAIL_SIZE = "w500";
    private static final String POSTER_SIZE = "w780";
    private static final String API_KEY = "api_key";

    // Fill in your personal API key in this constant :
    private static final String KEY = "";

    // URL builder to query movies list in the welcome screen
    public static String moviesUrlBuilder(String sortingSelection) {

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME)
                .authority(API_AUTHORITY)
                .appendPath(PRE_PATH_3)
                .appendPath(PRE_PATH_MOVIE)
                .appendPath(sortingSelection)
                .appendQueryParameter(API_KEY, KEY);

        return uriBuilder.toString();
    }

    // URL builder to open movie url from the detail screen
    public static String movieUrlBuilder(int movieId) {

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME)
                .authority(SITE_AUTHORITY)
                .appendPath(PRE_PATH_MOVIE)
                .appendPath(String.valueOf(movieId));

        return uriBuilder.toString();
    }

    // URL builder to query movie trailers in the detail screen
    public static String trailersUrlBuilder(int movieId) {

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME)
                .authority(API_AUTHORITY)
                .appendPath(PRE_PATH_3)
                .appendPath(PRE_PATH_MOVIE)
                .appendPath(String.valueOf(movieId))
                .appendPath(PRE_PATH_VIDEO)
                .appendQueryParameter(API_KEY, KEY);

        return uriBuilder.toString();
    }

    // URL builder to query movie review in the detail screen
    public static String reviewsUrlBuilder(int movieId) {

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME)
                .authority(API_AUTHORITY)
                .appendPath(PRE_PATH_3)
                .appendPath(PRE_PATH_MOVIE)
                .appendPath(String.valueOf(movieId))
                .appendPath(PRE_PATH_REVIEW)
                .appendQueryParameter(API_KEY, KEY);

        return uriBuilder.toString();
    }

    // URL builder for movie posters in the welcome screen
    public static String moviePosterUrlBuilder(String moviePath) {

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME)
                .authority(IMAGE_AUTHORITY)
                .appendPath(PRE_PATH_T)
                .appendPath(PRE_PATH_P)
                .appendPath(POSTER_SIZE)
                .appendPath(moviePath);

        return uriBuilder.toString();
    }

    // URL builder for movies thumbnails in the details screen
    public static String movieThumbnailUrlBuilder(String moviePath) {

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME)
                .authority(IMAGE_AUTHORITY)
                .appendPath(PRE_PATH_T)
                .appendPath(PRE_PATH_P)
                .appendPath(THUMBNAIL_SIZE)
                .appendPath(moviePath);

        return uriBuilder.toString();
    }

    // URL builder to get the YouTube video thumbnail
    public static String trailerThumbnailUrlBuilder(String key) {

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME)
                .authority(YOUTUBE_IMAGE_AUTHORITY)
                .appendPath(PRE_PATH_VI)
                .appendPath(key)
                .appendPath(JPG);

        return uriBuilder.toString();
    }
}
