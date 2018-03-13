package com.example.android.popularmovies.ui.welcomescreen;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.databinding.ActivityMainBinding;
import com.example.android.popularmovies.ui.detailsscreen.DetailActivity;
import com.example.android.popularmovies.ui.detailsscreen.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.services.UrlBuilder.queryUrlBuilder;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    // Tag for log messages
    public static final String LOG_TAG = MainActivity.class.getName();

    private MovieAdapter mAdapter;
    private List<Movie> moviesList = new ArrayList<>();
    private static final int MOVIE_LOADER_ID = 1;

    // Store the binding
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the content view
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mAdapter = new MovieAdapter(this, moviesList);

        binding.gridview.setAdapter(mAdapter);

        binding.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                launchDetailActivity(position);
            }
        });

        doSearch();
    }

    private void launchDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("Movie", moviesList.get(position));
        startActivity(intent);
    }

    // Launch the network connection to get the data from the Movie database API
    private void doSearch() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
            binding.loadingSpinner.setVisibility(View.VISIBLE);
        } else {
            // Otherwise, display a network issue message
            // First, hide loading indicator so error message will be visible
            binding.loadingSpinner.setVisibility(View.GONE);
            binding.emptyView.setImageResource(R.drawable.no_internet_escargot);
            binding.emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        return new MovieLoader(this, queryUrlBuilder());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        // Hide loading indicator because the data has been loaded
        binding.loadingSpinner.setVisibility(View.GONE);

        //Clear the adapter of previous movies data
       mAdapter.setMovieInfoList(null);

        // If there is a valid list of movies, then add them to the adapter's data set.
        // This will trigger the GridView to update itself.
        if (movies != null && !movies.isEmpty()) {
            mAdapter.setMovieInfoList(movies);
            mAdapter.notifyDataSetChanged();
            moviesList = new ArrayList<>(movies);
        }
        else {
            // Set empty view to display the "no results found" image
            binding.emptyView.setImageResource(R.drawable.sorry_no_results_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        //Loader reset, so we can clear out our existing data.
        mAdapter.setMovieInfoList(null);
    }
}