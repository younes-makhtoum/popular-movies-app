package com.example.android.popularmovies.ui.welcomescreen;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.databinding.ActivityMainBinding;
import com.example.android.popularmovies.services.data.MovieContract.MovieEntry;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.registerable.connection.Connectable;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.services.UrlBuilder.moviesUrlBuilder;

public class MainActivity extends AppCompatActivity {

    // Tag for log messages
    public static final String LOG_TAG = MainActivity.class.getName();

    // Used to handle the user's chosen sort preferences for movies
    private String sortBy;

    // RecyclerView adapter instance
    private MovieAdapter movieAdapter;

    // Loader ID's
    private static final int REMOTE_MOVIE_LOADER_ID = 101;
    private static final int FAVORITES_MOVIE_LOADER_ID = 102;

    // Store the binding
    private ActivityMainBinding binding;

    // Used to check the internet connection changes
    Merlin merlin;
    // Used to check the instant internet connection status
    MerlinsBeard merlinsBeard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sortBy = sharedPrefs.getString(
                getString(R.string.settings_sort_by_key),
                getString(R.string.settings_sort_by_default)
        );

        if (sortBy.equals(getString(R.string.settings_sort_by_most_popular_value))) {
            setTitle(getString(R.string.main_most_popular_title));
        } else if (sortBy.equals(getString(R.string.settings_sort_by_top_rated_value))) {
            setTitle(getString(R.string.main_top_rated_title));
        } else {
            setTitle(getString(R.string.main_favorites_title));
        }

        // Inflate the content view
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // set GridLayoutManager with default vertical orientation and two columns to the RecyclerView
        binding.recyclerMain.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        // Enable performance optimizations (significantly smoother scrolling),
        // by setting the following parameters on the RecyclerView
        binding.recyclerMain.recyclerView.setHasFixedSize(true);
        binding.recyclerMain.recyclerView.setItemViewCacheSize(20);
        binding.recyclerMain.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerMain.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // Add space between grid items in the RecyclerView,
        SpacesItemDecoration decoration = new SpacesItemDecoration(4);
        binding.recyclerMain.recyclerView.addItemDecoration(decoration);

        movieAdapter = new MovieAdapter(this);

        binding.recyclerMain.recyclerView.setAdapter(movieAdapter);

        merlin = new Merlin.Builder().withConnectableCallbacks().build(getApplicationContext());
        merlinsBeard = MerlinsBeard.from(getApplicationContext());

        // Internet status activation is monitored with this listener registration
        merlin.registerConnectable(new Connectable() {
            @Override
            public void onConnect() {
                // Only start a new query if the sort selection requires internet connection
                if (!sortBy.equals(getString(R.string.settings_sort_by_favorites_value))) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.recyclerMain.recyclerView.setVisibility(View.GONE);
                            loadMovies(REMOTE_MOVIE_LOADER_ID, false);
                        }
                    });
                }
            }
        });

        // Make sure to show the 'noInternetDisclaimer' at app launch,
        // if no internet is detected, and the sorting favorites sorting is not selected
        if (!merlinsBeard.isConnected() && !sortBy.equals(getString(R.string.settings_sort_by_favorites_value))) {
            noInternetDisclaimer();
        }

        if (sortBy.equals(getString(R.string.settings_sort_by_favorites_value))) {
            loadMovies(FAVORITES_MOVIE_LOADER_ID, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Bind the merlin listener so that internet status changes are monitored
        merlin.bind();
    }

    @Override
    protected void onPause() {
        // Unbind the merlin listener as the activity is on pause
        merlin.unbind();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        if (sortBy.equals(getString(R.string.settings_sort_by_favorites_value))) {
            loadMovies(FAVORITES_MOVIE_LOADER_ID, true);
        }
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Helper method to load movies either remotely from API or from the user's favorites db.
    private void loadMovies(int loaderID, boolean isRefreshed) {
        LoaderManager loaderManager = getLoaderManager();
        switch (loaderID) {
            case REMOTE_MOVIE_LOADER_ID:
                loaderManager.initLoader(REMOTE_MOVIE_LOADER_ID, null, remoteMovies);
                binding.recyclerMain.recyclerView.setVisibility(View.GONE);
                binding.recyclerMain.loadingSpinner.setVisibility(View.VISIBLE);
                break;
            case FAVORITES_MOVIE_LOADER_ID:
                if(!isRefreshed) {
                    loaderManager.initLoader(FAVORITES_MOVIE_LOADER_ID, null, favouriteMovies);
                }
                else {
                    loaderManager.restartLoader(FAVORITES_MOVIE_LOADER_ID, null, favouriteMovies).forceLoad();
                }
                break;
        }
    }

    // Launch the network connection to get movies from the movie.db API
    LoaderManager.LoaderCallbacks<List<Movie>> remoteMovies = new LoaderManager.LoaderCallbacks<List<Movie>>() {
        @Override
        public Loader<List<Movie>> onCreateLoader(int loaderID, Bundle args) {
            return new RemoteLoader(MainActivity.this, moviesUrlBuilder(sortBy));
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
            // Hide loading indicator because the data has been loaded
            binding.recyclerMain.loadingSpinner.setVisibility(View.GONE);
            // Clear the adapter of previous movies data
            movieAdapter.setMovieInfoList(null);
            // If there is a valid list of movies, then add them to the adapter's data set.
            // This will trigger the GridView to update itself.
            if (movies != null && !movies.isEmpty()) {
                movieAdapter.setMovieInfoList(movies);
                movieAdapter.notifyDataSetChanged();
                // Show the successful loading layout
                binding.recyclerMain.recyclerView.setVisibility(View.VISIBLE);
            } else {
                // Set empty view to display the "no results found" image
                binding.recyclerMain.emptyView.setImageResource(R.drawable.no_results);
                binding.recyclerMain.emptyView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {
            // Loader reset, so we can clear out our existing data.
            movieAdapter.setMovieInfoList(null);
        }
    };

    // Get the movies from the user's favorites database
    LoaderManager.LoaderCallbacks<Cursor> favouriteMovies = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String[] projection = {
                MovieEntry.COLUMN_MOVIE_ID,
                MovieEntry.COLUMN_MOVIE_NAME,
                MovieEntry.COLUMN_MOVIE_USER_RATING,
                MovieEntry.COLUMN_MOVIE_POSTER,
                MovieEntry.COLUMN_MOVIE_PLOT_SYNOPSIS,
                MovieEntry.COLUMN_MOVIE_RELEASE_DATE};

            return new CursorLoader(MainActivity.this,
                    MovieEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null);
        }
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            // Clear the adapter of previous movies data
            movieAdapter.setMovieInfoList(null);
            List<Movie> favoriteMovies = new ArrayList<>();

            Log.v(LOG_TAG,"LOG// cursor = " + cursor);

            if(cursor != null && cursor.moveToFirst()) {
                do {
                    int movieId = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID));
                    float movieUserRating = cursor.getFloat(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_USER_RATING));
                    String movieTitle = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_NAME));
                    String moviePoster = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_POSTER));
                    String moviePlotSynopsis = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_PLOT_SYNOPSIS));
                    String movieReleaseDate = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_RELEASE_DATE));
                    favoriteMovies.add(new Movie(movieId, movieUserRating, movieTitle, moviePoster, moviePlotSynopsis, movieReleaseDate));
                } while (cursor.moveToNext());
                cursor.close();
            }
            if(!favoriteMovies.isEmpty()) {
                movieAdapter.setMovieInfoList(favoriteMovies);
                movieAdapter.notifyDataSetChanged();
                // Show the successful loading layout
                binding.recyclerMain.recyclerView.setVisibility(View.VISIBLE);
            } else {
                // Set empty view to display the no favorites message
                noFavoritesDisclaimer();
            }
        }
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            // Loader reset, so we can clear out our existing data.
            movieAdapter.setMovieInfoList(null);
        }
    };

    // Helper method to display a "No internet" disclaimer
    private void noInternetDisclaimer() {
        binding.recyclerMain.recyclerView.setVisibility(View.GONE);
        binding.recyclerMain.emptyView.setImageResource(R.drawable.no_internet_connection);
        binding.recyclerMain.emptyView.setVisibility(View.VISIBLE);
    }

    // Helper method to display a "No favorites" disclaimer
    private void noFavoritesDisclaimer() {
        binding.recyclerMain.recyclerView.setVisibility(View.GONE);
        binding.recyclerMain.emptyView.setImageResource(R.drawable.no_favorites);
        binding.recyclerMain.emptyView.setVisibility(View.VISIBLE);
    }
}

