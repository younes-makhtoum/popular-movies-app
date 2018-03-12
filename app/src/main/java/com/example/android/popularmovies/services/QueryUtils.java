package com.example.android.popularmovies.services;

import android.util.Log;
import com.example.android.popularmovies.ui.detailsscreen.Movie;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class QueryUtils {

    private static final String VOTE_AVERAGE = "vote_average";
    private static final String TITLE = "title";
    private static final String POSTER_PATH = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";

    public static Movie parseMovieJson(String json) {

        float voteAverage = 0;
        String title = "";
        String posterPath = "";
        String overview = "";
        String releaseDate = "";

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject clickedMovie = new JSONObject(json);

            if (clickedMovie.has(VOTE_AVERAGE)) {
                // Extract the value for the key called "vote_average"
                voteAverage = BigDecimal.valueOf(clickedMovie.getDouble(VOTE_AVERAGE)).floatValue();
            }

            if (clickedMovie.has(TITLE)) {
                // Extract the value for the key called "title"
                title = clickedMovie.optString(TITLE);
            }

            if (clickedMovie.has(POSTER_PATH)) {
                // Extract the value for the key called "poster_path"
                posterPath = clickedMovie.optString(POSTER_PATH);
            }

            if (clickedMovie.has(OVERVIEW)) {
                // Extract the value for the key called "overview"
                overview = clickedMovie.optString(OVERVIEW);
            }

            if (clickedMovie.has(RELEASE_DATE)) {
                // Extract the value for the key called "release_date"
                releaseDate = clickedMovie.optString(RELEASE_DATE);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
        }

        // Return the movie with its related info :
        return new Movie(voteAverage, title, posterPath, overview, releaseDate);
    }
}
