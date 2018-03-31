package com.example.android.popularmovies.services.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.popularmovies.services.data.MovieContract.MovieEntry;

/**
 * Database helper for Movies app. It manages database creation and version management.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    /** Tag for the log messages */
    public static final String LOG_TAG = MovieDbHelper.class.getSimpleName();

    /** Declaration of database name and initialisation of its version number */
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link MovieDbHelper}.
     *
     * @param context of the app
     */
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This method is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the movies table
        String SQL_CREATE_MOVIES_TABLE =  "CREATE TABLE " + MovieEntry.TABLE_NAME + " ("
                + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieEntry.COLUMN_MOVIE_ID + " INT, "
                + MovieEntry.COLUMN_MOVIE_NAME + " TEXT, "
                + MovieEntry.COLUMN_MOVIE_USER_RATING + " FLOAT, "
                + MovieEntry.COLUMN_MOVIE_POSTER + " TEXT, "
                + MovieEntry.COLUMN_MOVIE_PLOT_SYNOPSIS + " TEXT, "
                + MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
