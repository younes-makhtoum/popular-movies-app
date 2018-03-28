package com.example.android.popularmovies.ui.detailsscreens.trailers;

import android.content.Context;

import com.example.android.popularmovies.services.QueryUtils;

import java.util.List;

public class TrailerLoader extends android.support.v4.content.AsyncTaskLoader<List<Trailer>> {

    // Tag for log messages
    private static final String LOG_TAG = TrailerLoader.class.getName();

    // Query URL
    private String mUrl;

    /**
     * Constructs a new {@link TrailerLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */

    public TrailerLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Trailer> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of movies.
        return QueryUtils.fetchTrailerData(mUrl);
    }
}