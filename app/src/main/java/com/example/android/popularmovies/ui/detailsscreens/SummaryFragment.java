package com.example.android.popularmovies.ui.detailsscreens;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.databinding.FragmentSummaryBinding;
import com.example.android.popularmovies.ui.welcomescreen.Movie;
import com.squareup.picasso.Picasso;

import static com.example.android.popularmovies.services.UrlBuilder.movieThumbnailUrlBuilder;
import static com.example.android.popularmovies.services.UrlBuilder.movieUrlBuilder;

import com.example.android.popularmovies.services.data.MovieContract.MovieEntry;

public class SummaryFragment extends Fragment {

    // Tag for log messages
    public static final String LOG_TAG = SummaryFragment.class.getName();

    // Store the binding
    private FragmentSummaryBinding binding;

    // Movie object instance declaration to handle the received parcelable
    private Movie selectedMovie;

    // Tells whether the selected movie is among the favorites
    private boolean isFavorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSummaryBinding.bind(inflater.inflate(R.layout.fragment_summary, container, false));

        View rootView = binding.getRoot();

        selectedMovie = ((DetailActivity)this.getActivity()).getSelectedMovie();

        Picasso.with(getContext())
                .load(movieThumbnailUrlBuilder(selectedMovie.getPoster()))
                .placeholder(R.drawable.thumbnail_place_holder)
                .into(binding.moviePosterDetail);

        String[] projection = { MovieEntry.COLUMN_MOVIE_ID };
        String selectionClause = MovieEntry.COLUMN_MOVIE_ID + " = ?";
        String[] selectionArgs = {"" + String.valueOf(selectedMovie.getId())};

        // Query the movie favorites database to check if the selected movie is present,
        // and display an appropriate appearance of the star ImageButton.
        Cursor cursor = getContext().getContentResolver().query(
                MovieEntry.CONTENT_URI,
                projection, selectionClause, selectionArgs, null);

        if (cursor == null) {
            Log.e(LOG_TAG, "Error in querying the movie database");
            // If the cursor is empty, the provider found no matches
        } else if (cursor.getCount() < 1) {
            isFavorite = false;
            binding.starButton.setSelected(false);
        } else {
            isFavorite = true;
            binding.starButton.setSelected(true);
        }

        assert cursor != null;
        cursor.close();

        binding.moviePosterDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieUrlBuilder(selectedMovie.getId())));
                try {
                    getContext().startActivity(webIntent);
                } catch (ActivityNotFoundException ex) {
                    getContext().startActivity(webIntent);
                }
            }
        });

        binding.starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavorite) {
                    saveMovieInFavorites();
                    binding.starButton.setSelected(true);
                    isFavorite = true;
                } else {
                    removeMovieFromFavorites();
                    binding.starButton.setSelected(false);
                    isFavorite = false;
                }
            }
        });

        populateUI();

        return rootView;
    }

    // Helper method to populate the summary screen with detailed data about the selected movie.
    private void populateUI() {

        if(!selectedMovie.getTitle().isEmpty()) {
            binding.title.setText(selectedMovie.getTitle());
        }
        else {
            binding.title.setVisibility(View.GONE);
        }

        if(!selectedMovie.getReleaseDate().isEmpty()) {
            binding.releaseDate.setText(selectedMovie.getReleaseDate().substring(0,4));
        }
        else {
            binding.releaseDate.setVisibility(View.GONE);
        }

        if(!String.valueOf(selectedMovie.getUserRating()).isEmpty()) {
            binding.userRating.setText(String.valueOf(selectedMovie.getUserRating()));
        }
        else {
            binding.userRating.setVisibility(View.GONE);
        }

        if(!selectedMovie.getPlotSynopsis().isEmpty()) {
            binding.plotSynopsis.setText(selectedMovie.getPlotSynopsis());
        }
        else {
            binding.plotSynopsis.setVisibility(View.GONE);
        }
    }

    // Save a movie as favorite
    private void saveMovieInFavorites() {

        ContentValues values = new ContentValues();

        values.put(MovieEntry.COLUMN_MOVIE_ID, selectedMovie.getId());
        values.put(MovieEntry.COLUMN_MOVIE_NAME, selectedMovie.getTitle());
        values.put(MovieEntry.COLUMN_MOVIE_USER_RATING, selectedMovie.getUserRating());
        values.put(MovieEntry.COLUMN_MOVIE_POSTER, selectedMovie.getPoster());
        values.put(MovieEntry.COLUMN_MOVIE_PLOT_SYNOPSIS, selectedMovie.getPlotSynopsis());
        values.put(MovieEntry.COLUMN_MOVIE_RELEASE_DATE, selectedMovie.getReleaseDate());

        Uri newUri = getContext().getContentResolver().insert(MovieEntry.CONTENT_URI, values);

        if (newUri == null) {
            Toast.makeText(getContext(), getString(R.string.save_movie_as_favorite_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.save_movie_as_favorite_succeeded),
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Remove a movie from favorites
    private void removeMovieFromFavorites() {

        String selectionClause = MovieEntry.COLUMN_MOVIE_ID + " = ?";
        String[] selectionArgs = {"" + String.valueOf(selectedMovie.getId())};
        int rowsDeleted = getContext().getContentResolver().delete(MovieEntry.CONTENT_URI, selectionClause, selectionArgs);

        if (rowsDeleted == 0) {
            Toast.makeText(getContext(), getString(R.string.remove_movie_from_favorites_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.remove_movie_from_favorites_succeeded),
                    Toast.LENGTH_SHORT).show();
        }
    }
}