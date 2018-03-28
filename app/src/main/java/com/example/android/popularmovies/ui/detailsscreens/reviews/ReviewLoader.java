package com.example.android.popularmovies.ui.detailsscreens.reviews;

import android.content.Context;

import com.example.android.popularmovies.services.QueryUtils;

import java.util.List;

public class ReviewLoader extends android.support.v4.content.AsyncTaskLoader<List<Review>> {

    // Tag for log messages
    private static final String LOG_TAG = ReviewLoader.class.getName();

    // Query URL
    private String mUrl;

    /**
     * Constructs a new {@link ReviewLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */

    public ReviewLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Review> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of movies.
        return QueryUtils.fetchReviewData(mUrl);
    }
}