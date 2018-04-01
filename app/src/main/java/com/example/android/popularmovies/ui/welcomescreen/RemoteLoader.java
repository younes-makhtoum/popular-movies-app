package com.example.android.popularmovies.ui.welcomescreen;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.popularmovies.services.QueryUtils;

import java.util.List;

public class RemoteLoader extends AsyncTaskLoader<List<Movie>> {

    // Tag for log messages
    private static final String LOG_TAG = RemoteLoader.class.getName();

    // Query URL
    private String mUrl;

    /**
     * Constructs a new {@link RemoteLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */

    public RemoteLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of movies.
        return QueryUtils.fetchMovieData(mUrl);
    }
}