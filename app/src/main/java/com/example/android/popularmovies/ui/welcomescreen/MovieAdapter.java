package com.example.android.popularmovies.ui.welcomescreen;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.ui.detailsscreens.DetailActivity;
import com.example.android.popularmovies.ui.detailsscreens.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.services.UrlBuilder.moviePosterUrlBuilder;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    // Tag for log messages
    public static final String LOG_TAG = MovieAdapter.class.getName();

    private final Context context;
    private List<Movie> moviesList = new ArrayList<>();

    public MovieAdapter(Context context, List<Movie> moviesList) {
        this.context = context;
        this.moviesList = moviesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView posterImageView;

        private MyViewHolder(View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.movie_poster_grid);
        }
    }

    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MyViewHolder holder, final int position) {

        Movie currentMovie = moviesList.get(position);

        Picasso.with(context)
                .load(moviePosterUrlBuilder(currentMovie.getPoster()))
                .placeholder(R.drawable.movie_poster_not_available)
                .into(holder.posterImageView);

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open another activity on item click
                Intent intent = new Intent(context, DetailActivity.class);
                // put movie object in the Intent
                intent.putExtra("Movie", moviesList.get(position));
                // start Intent
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (moviesList == null) {
            return 0;
        } else {
            return moviesList.size();
        }
    }

    // Helper method to set the actual movies list into the recycler view on the activity
    public void setMovieInfoList(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }
}