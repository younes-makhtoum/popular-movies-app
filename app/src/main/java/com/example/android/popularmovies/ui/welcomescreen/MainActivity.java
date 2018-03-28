package com.example.android.popularmovies.ui.welcomescreen;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.popularmovies.R;

import com.example.android.popularmovies.databinding.ActivityMainBinding;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.registerable.connection.Connectable;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.services.UrlBuilder.moviesUrlBuilder;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    // Tag for log messages
    public static final String LOG_TAG = MainActivity.class.getName();

    private List<Movie> moviesList = new ArrayList<>();
    private MovieAdapter movieAdapter;

    private static final int MOVIE_LOADER_ID = 101;

    // Store the binding
    private ActivityMainBinding binding;

    // Used to check the internet connection changes
    Merlin merlin;
    // Used to check the instant internet connection status
    MerlinsBeard merlinsBeard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the content view
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        
        // set GridLayoutManager with default vertical orientation and two columns to the RecyclerView
        binding.recyclerMain.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

        // Enable performance optimizations (significantly smoother scrolling),
        // by setting the following parameters on the RecyclerView
        binding.recyclerMain.recyclerView.setHasFixedSize(true);
        binding.recyclerMain.recyclerView.setItemViewCacheSize(20);
        binding.recyclerMain.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerMain.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // Add space between grid items in the RecyclerView,
        SpacesItemDecoration decoration = new SpacesItemDecoration(4);
        binding.recyclerMain.recyclerView.addItemDecoration(decoration);

        movieAdapter = new MovieAdapter(this, moviesList);

        binding.recyclerMain.recyclerView.setAdapter(movieAdapter);

        merlin = new Merlin.Builder().withConnectableCallbacks().build(getApplicationContext());
        merlinsBeard = MerlinsBeard.from(getApplicationContext());

        // Internet status activation is monitored with this listener registration
        merlin.registerConnectable(new Connectable() {
            @Override
            public void onConnect() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.recyclerMain.emptyView.setVisibility(View.GONE);
                        queryMovies();
                    }
                });
            }
        });

       // Make sure to show the 'noInternetDisclaimer' at app launch, if no internet is detected.
        if(!merlinsBeard.isConnected()) {
            noInternetDisclaimer();
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

    // No internet disclaimer
    private void noInternetDisclaimer(){
        binding.recyclerMain.recyclerView.setVisibility(View.GONE);
        binding.recyclerMain.emptyView.setImageResource(R.drawable.no_internet_connection);
        binding.recyclerMain.emptyView.setVisibility(View.VISIBLE);
    }

    // Launch the network connection to get the data from the Movie database API
    private void queryMovies() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        binding.recyclerMain.recyclerView.setVisibility(View.GONE);
        binding.recyclerMain.loadingSpinner.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortBy = sharedPrefs.getString(
                getString(R.string.settings_sort_by_key),
                getString(R.string.settings_sort_by_default)
        );

        return new MovieLoader(this, moviesUrlBuilder(sortBy));
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
            moviesList = new ArrayList<>(movies);
        }
        else {
            // Set empty view to display the "no results found" image
            binding.recyclerMain.emptyView.setImageResource(R.drawable.no_results);
            binding.recyclerMain.emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        //Loader reset, so we can clear out our existing data.
        movieAdapter.setMovieInfoList(null);
    }
}