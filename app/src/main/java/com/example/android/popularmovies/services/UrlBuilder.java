package com.example.android.popularmovies.services;

import android.net.Uri;

public class UrlBuilder {

    private static final String SCHEME = "http";
    private static final String AUTHORITY = "image.tmdb.org";
    private static final String PRE_PATH_T = "t";
    private static final String PRE_PATH_P = "p";
    private static final String POSTER_SIZE = "w780";

    public static String urlBuilder(String moviePath) {

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(PRE_PATH_T)
                .appendPath(PRE_PATH_P)
                .appendPath(POSTER_SIZE)
                .appendPath(moviePath);

        return uriBuilder.toString();
    }
}
