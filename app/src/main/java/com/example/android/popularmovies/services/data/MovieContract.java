package com.example.android.popularmovies.services.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class MovieContract {

    // To prevent someone from accidentally instantiating the contract class, an empty constructor is given
    private MovieContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    /**
     * Inner class that defines constant values for the movies database table.
     * Each entry in the table represents a single movie.
     */
    public static final class MovieEntry implements BaseColumns {

        /**
         * The content URI to access the movie data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of movies.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single movie.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        /**
         * Name of database table for movies
         */
        public final static String TABLE_NAME = "movies";

        /**
         * Unique ID number for the movie (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Remote ID of the movie.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_MOVIE_ID = "id";

        /**
         * Name of the movie.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_MOVIE_NAME = "name";

        /**
         * User rating of the movie.
         * <p>
         * Type: FLOAT
         */
        public final static String COLUMN_MOVIE_USER_RATING = "user_rating";

        /**
         * Poster URL of the movie.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_MOVIE_POSTER = "poster";

        /**
         * Plot synopsis of the movie.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_MOVIE_PLOT_SYNOPSIS = "plot_synopsis";

        /**
         * Release date of the movie.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_MOVIE_RELEASE_DATE = "release_date";
    }
}
