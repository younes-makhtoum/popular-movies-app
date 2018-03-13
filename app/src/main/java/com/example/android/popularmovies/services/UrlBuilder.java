package com.example.android.popularmovies.services;

import android.net.Uri;

public class UrlBuilder {

    private static final String SCHEME = "http";
    private static final String API_AUTHORITY = "api.themoviedb.org";
    private static final String IMAGE_AUTHORITY = "image.tmdb.org";
    private static final String PRE_PATH_MOVIE = "movie";
    private static final String PRE_PATH_POPULAR = "popular";
    private static final String PRE_PATH_3 = "3";
    private static final String PRE_PATH_T = "t";
    private static final String PRE_PATH_P = "p";
    private static final String POSTER_SIZE = "w780";
    private static final String API_KEY = "api_key";

    // Fill in your personal API key in this constant :
    private static final String KEY = "";

    public static String queryUrlBuilder() {

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME)
                .authority(API_AUTHORITY)
                .appendPath(PRE_PATH_3)
                .appendPath(PRE_PATH_MOVIE)
                .appendPath(PRE_PATH_POPULAR)
                .appendQueryParameter(API_KEY, KEY);

        return uriBuilder.toString();
    }

    public static String posterUrlBuilder(String moviePath) {

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME)
                .authority(IMAGE_AUTHORITY)
                .appendPath(PRE_PATH_T)
                .appendPath(PRE_PATH_P)
                .appendPath(POSTER_SIZE)
                .appendPath(moviePath);

        return uriBuilder.toString();
    }
}
