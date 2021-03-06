package com.example.android.popularmovies.services;

import android.text.TextUtils;
import android.util.Log;
import com.example.android.popularmovies.ui.welcomescreen.Movie;
import com.example.android.popularmovies.ui.detailsscreens.reviews.Review;
import com.example.android.popularmovies.ui.detailsscreens.trailers.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    //Tag for log messages
    private static final String LOG_TAG = QueryUtils.class.getName();

    // Constants referring to the name of the keys in the JSON objects
    private static final String ID = "id";
    private static final String URL = "url";
    private static final String KEY = "key";
    private static final String NAME = "name";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";
    private static final String OVERVIEW = "overview";
    private static final String POSTER_PATH = "poster_path";
    private static final String RELEASE_DATE = "release_date";
    private static final String VOTE_AVERAGE = "vote_average";

    /**
     * Create a private constructor,
     * because no one should ever create a {@link QueryUtils} object.
     */
    private QueryUtils() {
    }

    // Query the Movies database API for movies
    public static List<Movie> fetchMovieData(String requestUrl) {
        return extractMoviesFromJSON(queryRequest(requestUrl));
    }

    // Query the Movie database API for trailers
    public static List<Trailer> fetchTrailerData(String requestUrl) {
        return extractTrailersFromJSON(queryRequest(requestUrl));
    }
    
    // Query the Movie database API for reviews
    public static List<Review> fetchReviewData(String requestUrl) {
        return extractReviewsFromJSON(queryRequest(requestUrl));
    }
    
    public static String queryRequest(String requestUrl){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Create URL object
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        return jsonResponse;
    }

    // Returns new URL object from the given string URL.
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL,
     * and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movie JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String,
     * which contains the whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Movie} objects,
     * that has been built up from parsing the given JSON response.
     */
    public static List<Movie> extractMoviesFromJSON(String moviesJSON) {

        if (TextUtils.isEmpty(moviesJSON)) {
            return null;
        }

        int id = 0;
        float voteAverage = 0;
        String title = "";
        String posterPath = "";
        String overview = "";
        String releaseDate = "";

        List<Movie> movies = new ArrayList<>();

        try {
            JSONObject response = new JSONObject(moviesJSON);
            JSONArray moviesArray = response.optJSONArray("results");

            for (int i = 0; i < moviesArray.length(); i++) {

                JSONObject currentMovie = moviesArray.optJSONObject(i);

                if (currentMovie.has(ID)) {
                    // Extract the value for the key called "id"
                    id = currentMovie.getInt(ID);
                }
                if (currentMovie.has(VOTE_AVERAGE)) {
                    // Extract the value for the key called "vote_average"
                    voteAverage = BigDecimal.valueOf(currentMovie.getDouble(VOTE_AVERAGE)).floatValue();
                }
                if (currentMovie.has(TITLE)) {
                    // Extract the value for the key called "title"
                    title = currentMovie.optString(TITLE);
                }
                if (currentMovie.has(POSTER_PATH)) {
                    // Extract the value for the key called "poster_path"
                    posterPath = currentMovie.optString(POSTER_PATH).substring(1);
                }
                if (currentMovie.has(OVERVIEW)) {
                    // Extract the value for the key called "overview"
                    overview = currentMovie.optString(OVERVIEW);
                }
                if (currentMovie.has(RELEASE_DATE)) {
                    // Extract the value for the key called "release_date"
                    releaseDate = currentMovie.optString(RELEASE_DATE);
                }
                Movie movie = new Movie(id, voteAverage, title, posterPath, overview, releaseDate);
                movies.add(movie);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash.
            // Print a log message with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
        }

        // Return the list of movies :
        return movies;
    }

    /**
     * Return a list of {@link Trailer} objects,
     * that has been built up from parsing the given JSON response.
     */
    public static List<Trailer> extractTrailersFromJSON(String trailersJSON) {

        if (TextUtils.isEmpty(trailersJSON)) {
            return null;
        }

        String id = "";
        String key = "";
        String name = "";

        List<Trailer> trailers = new ArrayList<>();

        try {
            JSONObject response = new JSONObject(trailersJSON);
            JSONArray trailersArray = response.optJSONArray("results");

            for (int i = 0; i < trailersArray.length(); i++) {

                JSONObject currentTrailer = trailersArray.optJSONObject(i);
                if (currentTrailer.has(ID)) {
                    // Extract the value for the key called "id"
                    id = currentTrailer.optString(ID);
                }
                if (currentTrailer.has(KEY)) {
                    // Extract the value for the key called "key"
                    key = currentTrailer.optString(KEY);
                }
                if (currentTrailer.has(NAME)) {
                    // Extract the value for the key called "name"
                    name = currentTrailer.optString(NAME);
                }
                Trailer trailer = new Trailer(id, key, name);
                trailers.add(trailer);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash.
            // Print a log message with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
        }
        // Return the list of movies :
        return trailers;
    }

    /**
     * Return a list of {@link Review} objects,
     * that has been built up from parsing the given JSON response.
     */
    public static List<Review> extractReviewsFromJSON(String reviewsJSON) {

        if (TextUtils.isEmpty(reviewsJSON)) {
            return null;
        }

        String id = "";
        String url = "";
        String author = "";
        String content = "";

        List<Review> reviews = new ArrayList<>();

        try {
            JSONObject response = new JSONObject(reviewsJSON);
            JSONArray reviewsArray = response.optJSONArray("results");

            for (int i = 0; i < reviewsArray.length(); i++) {

                JSONObject currentReview = reviewsArray.optJSONObject(i);
                if (currentReview.has(ID)) {
                    // Extract the value for the key called "name"
                    id = currentReview.optString(ID);
                }
                if (currentReview.has(URL)) {
                    // Extract the value for the key called "name"
                    url = currentReview.optString(URL);
                }
                if (currentReview.has(AUTHOR)) {
                    // Extract the value for the key called "id"
                    author = currentReview.optString(AUTHOR);
                }
                if (currentReview.has(CONTENT)) {
                    // Extract the value for the key called "key"
                    content = currentReview.optString(CONTENT);
                }
                Review review = new Review(id, url, author, content);
                reviews.add(review);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash.
            // Print a log message with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
        }
        // Return the list of movies :
        return reviews;
    }
}