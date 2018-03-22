package com.example.android.popularmovies.ui.detailsscreens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;

import com.example.android.popularmovies.databinding.FragmentTrailersBinding;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.services.UrlBuilder.trailersUrlBuilder;

public class TrailersFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Trailer>> {

    // Tag for log messages
    public static final String LOG_TAG = TrailersFragment.class.getName();

    // Store the binding
    private FragmentTrailersBinding binding;

    // Declare an instance of Movie
    private Movie selectedMovie;

    // Create a new trailer list object
    private List<Trailer> trailersList = new ArrayList<>();
    private TrailerAdapter trailerAdapter;

    private static final int TRAILER_LOADER_ID = 201;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTrailersBinding.bind(inflater.inflate(R.layout.fragment_trailers, container, false));

        View rootView = binding.getRoot();

        // set LinearLayoutManager to the RecyclerView
        binding.recyclerTrailers.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Enable performance optimizations (significantly smoother scrolling),
        // by setting the following parameters on the RecyclerView
        binding.recyclerTrailers.recyclerView.setHasFixedSize(true);
        binding.recyclerTrailers.recyclerView.setItemViewCacheSize(20);
        binding.recyclerTrailers.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerTrailers.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // Add divider line between items in the RecyclerView,
        binding.recyclerTrailers.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        trailerAdapter = new TrailerAdapter(getContext(), trailersList);

        binding.recyclerTrailers.recyclerView.setAdapter(trailerAdapter);

        selectedMovie = ((DetailActivity)this.getActivity()).getSelectedMovie();

        // Launch the network connection to get the data from the Movie database API
        android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(TRAILER_LOADER_ID, null, this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        selectedMovie = ((DetailActivity)this.getActivity()).getSelectedMovie();
    }

    @Override
    public android.support.v4.content.Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        return new TrailerLoader(getContext(), trailersUrlBuilder(selectedMovie.getId()));
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Trailer>> loader, List<Trailer> trailers) {
        // Hide loading indicator because the data has been loaded
        binding.recyclerTrailers.loadingSpinner.setVisibility(View.GONE);
        // Clear the adapter of previous movies data
        trailerAdapter.setTrailerInfoList(null);
        // If there is a valid list of movies, then add them to the adapter's data set.
        // This will trigger the GridView to update itself.
        if (trailers != null && !trailers.isEmpty()) {
            trailerAdapter.setTrailerInfoList(trailers);
            trailerAdapter.notifyDataSetChanged();
            // Show the successful loading layout
            binding.recyclerTrailers.recyclerView.setVisibility(View.VISIBLE);
            trailersList = new ArrayList<>(trailers);
        }
        else {
            // Set empty view to display the "no results found" image
            binding.recyclerTrailers.emptyView.setImageResource(R.drawable.sorry_no_results_found);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Trailer>> loader) {
        //Loader reset, so we can clear out our existing data.
        trailerAdapter.setTrailerInfoList(null);
    }
}