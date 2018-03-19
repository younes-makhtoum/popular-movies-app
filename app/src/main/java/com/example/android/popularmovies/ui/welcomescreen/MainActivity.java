package com.example.android.popularmovies.ui.welcomescreen;

import android.app.LoaderManager;
import android.content.Loader;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.AdapterView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.databinding.ActivityMainBinding;
import com.example.android.popularmovies.ui.detailsscreen.Movie;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.registerable.connection.Connectable;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.services.UrlBuilder.queryUrlBuilder;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    // Tag for log messages
    public static final String LOG_TAG = MainActivity.class.getName();

    private List<Movie> moviesList = new ArrayList<>();
    private MovieAdapter movieAdapter;

    private static final int MOVIE_LOADER_ID = 1;
    private static final String MOST_POPULAR_URL = "popular";
    private static final String TOP_RATED_URL = "top_rated";
    private static final String KEY_SORT_SELECTION = "KEY_SORT_SELECTION";

    // Sort selection default value
    private String sortingSelection = MOST_POPULAR_URL;

    // Sort selection default change status
    private boolean sortSelectionHasChanged = false;

    // Used to prevent the sort method spinner listener to do a search at activity launch
    private int checkSpinner = 0;

    // Store the binding
    private ActivityMainBinding binding;

    // Used to check the internet connection changes
    Merlin merlin;
    // Used to check the instant internet connection status
    MerlinsBeard merlinsBeard;

    // Shared Preferences variables
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the content view
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // set GridLayoutManager with default vertical orientation and two columns to the RecyclerView
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

        /* Enable performance optimizations (significantly smoother scrolling),
        * by setting the following parameters on the RecyclerView */
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemViewCacheSize(20);
        binding.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // Add space between grid items in the RecyclerView,
        SpacesItemDecoration decoration = new SpacesItemDecoration(4);
        binding.recyclerView.addItemDecoration(decoration);

        movieAdapter = new MovieAdapter(this, moviesList);

        binding.recyclerView.setAdapter(movieAdapter);

        merlin = new Merlin.Builder().withConnectableCallbacks().build(getApplicationContext());
        merlinsBeard = MerlinsBeard.from(getApplicationContext());

        // Create a shared preferences file in order to save the sort selection in the spinner
        sharedPref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        // Internet status activation is monitored with this listener registration
        merlin.registerConnectable(new Connectable() {
            @Override
            public void onConnect() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.emptyView.setVisibility(View.GONE);
                        // Make sure to only get the sort selection from shared preference,
                        // if a record already exist in the related file.
                        // Otherwise, query the API with default value of the sortSelection variable.
                        if (sharedPref.contains(KEY_SORT_SELECTION))  {
                            getSortSelection();
                        }
                        doSearch();
                    }
                });
            }
        });

       binding.sortingSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Don't perform a search from here at app launch (checkSpinner = 0)
                if(++checkSpinner > 1) {
                    // 0 is the default spinner position for the sorting by "Most popular"
                    if (position == 0) {
                        sortingSelection = MOST_POPULAR_URL;
                    // 1 is the spinner position for the sorting by "Top rated"
                    } else {
                        sortingSelection = TOP_RATED_URL;
                    }
                    saveSortSelection();
                    // Check the internet connection before sending a search request
                    if (merlinsBeard.isConnected()) {
                        movieAdapter.notifyDataSetChanged();
                        doSearch();
                    } else {
                        noInternetDisclaimer();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // As the selection and spinner never disappears from the activity's layout,
                // this method is never called, but mandatory to be implemented.
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

    // Saves the sort selection in shared preferences
    private void saveSortSelection(){
        sortSelectionHasChanged = true;
        sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putString(KEY_SORT_SELECTION, sortingSelection); // Storing string
        sharedPrefEditor.apply();
    }

    // Get the sort selection from shared preferences
    private void getSortSelection(){
        sortingSelection = sharedPref.getString(KEY_SORT_SELECTION, null); // getting String
        if (MOST_POPULAR_URL.equals(sortingSelection)) {
            binding.sortingSelector.setSelection(0);
            // 1 is the spinner position for the sorting by "Top rated"
        } else {
            binding.sortingSelector.setSelection(1);
        }
    }

    // No internet disclaimer
    private void noInternetDisclaimer(){
        binding.postersDisplayLayout.setVisibility(View.GONE);
        binding.emptyView.setImageResource(R.drawable.no_internet_escargot);
        binding.emptyView.setVisibility(View.VISIBLE);
    }

    // Launch the network connection to get the data from the Movie database API
    private void doSearch() {
        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();
        // Depending on the relevant case, initialize or restart the loader
        if(!sortSelectionHasChanged) {
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        }
        else {
            loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
        }
        binding.postersDisplayLayout.setVisibility(View.GONE);
        binding.loadingSpinner.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        return new MovieLoader(this, queryUrlBuilder(sortingSelection));
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        // Hide loading indicator because the data has been loaded
        binding.loadingSpinner.setVisibility(View.GONE);
        // Clear the adapter of previous movies data
        movieAdapter.setMovieInfoList(null);
        // If there is a valid list of movies, then add them to the adapter's data set.
        // This will trigger the GridView to update itself.
        if (movies != null && !movies.isEmpty()) {
            movieAdapter.setMovieInfoList(movies);
            movieAdapter.notifyDataSetChanged();
            // Show the successful loading layout
            binding.postersDisplayLayout.setVisibility(View.VISIBLE);
            moviesList = new ArrayList<>(movies);
            binding.sortingSelector.setEnabled(true);
        }
        else {
            // Set empty view to display the "no results found" image
            binding.emptyView.setImageResource(R.drawable.sorry_no_results_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        //Loader reset, so we can clear out our existing data.
        movieAdapter.setMovieInfoList(null);
    }
}