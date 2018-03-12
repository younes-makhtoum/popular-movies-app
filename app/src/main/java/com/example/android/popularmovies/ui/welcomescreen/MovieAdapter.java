package com.example.android.popularmovies.ui.welcomescreen;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.services.QueryUtils;
import com.example.android.popularmovies.ui.detailsscreen.Movie;
import com.squareup.picasso.Picasso;

import static com.example.android.popularmovies.services.UrlBuilder.urlBuilder;

class MovieAdapter extends BaseAdapter {

    public static final String LOG_TAG = MovieAdapter.class.getName();

    private final Context mContext;

    public MovieAdapter(Context c) {
        this.mContext = c;
    }

    public int getCount() {
        return 4;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load(urlBuilder((movieProvider(position).getPoster())))
                .into(imageView);

        return imageView;
    }

    private Movie movieProvider(int position) {
        String[] movies = mContext.getResources().getStringArray(R.array.movie_details);
        return QueryUtils.parseMovieJson(movies[position]);
    }
}