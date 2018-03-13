package com.example.android.popularmovies.ui.welcomescreen;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.ui.detailsscreen.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.services.UrlBuilder.posterUrlBuilder;

class MovieAdapter extends BaseAdapter {

    // Tag for log messages
    public static final String LOG_TAG = MovieAdapter.class.getName();

    private final Context mContext;
    private List<Movie> moviesList = new ArrayList<>();

    public MovieAdapter(Context c, List<Movie> moviesList) {
        this.mContext = c;
        this.moviesList = moviesList;
    }

    public int getCount() {
        if (moviesList == null) {
            return 0;
        } else {
            return moviesList.size();
        }
    }

    public Object getItem(int position) {
        return moviesList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie currentMovie = moviesList.get(position);
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        if (!currentMovie.getPoster().isEmpty()) {
            Picasso.with(mContext)
                    .load(posterUrlBuilder(currentMovie.getPoster()))
                    .into(imageView);
        }
        else {
            Picasso.with(mContext)
                    .load(R.drawable.movie_poster_not_available)
                    .into(imageView);
        }
        return imageView;
    }

    // Helper method to set the actual movies list into the gridview on the activity
    public void setMovieInfoList(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }
}