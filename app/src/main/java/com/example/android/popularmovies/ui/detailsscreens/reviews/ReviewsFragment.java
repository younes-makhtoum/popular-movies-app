package com.example.android.popularmovies.ui.detailsscreens.reviews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;

import com.example.android.popularmovies.databinding.FragmentReviewsBinding;
import com.example.android.popularmovies.ui.detailsscreens.DetailActivity;
import com.example.android.popularmovies.ui.welcomescreen.Movie;
import com.novoda.merlin.MerlinsBeard;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.services.UrlBuilder.reviewsUrlBuilder;

public class ReviewsFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Review>> {

    // Tag for log messages
    public static final String LOG_TAG = ReviewsFragment.class.getName();

    // Store the binding
    private FragmentReviewsBinding binding;

    // Declare an instance of Movie
    private Movie selectedMovie;

    // Create a new review list object
    private List<Review> reviewsList = new ArrayList<>();
    private ReviewAdapter reviewAdapter;

    private static final int REVIEW_LOADER_ID = 201;

    private MerlinsBeard merlinsBeard;

    private boolean dataIsLoaded = false;
    private boolean noDataFound = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser & !dataIsLoaded) {
            loadData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewsBinding.bind(inflater.inflate(R.layout.fragment_reviews, container, false));

        View rootView = binding.getRoot();

        // set LinearLayoutManager to the RecyclerView
        binding.recyclerReviews.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Enable performance optimizations (significantly smoother scrolling),
        // by setting the following parameters on the RecyclerView
        binding.recyclerReviews.recyclerView.setHasFixedSize(true);
        binding.recyclerReviews.recyclerView.setItemViewCacheSize(20);
        binding.recyclerReviews.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerReviews.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // Add divider line between items in the RecyclerView,
        binding.recyclerReviews.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        reviewAdapter = new ReviewAdapter(getContext(), reviewsList);

        binding.recyclerReviews.recyclerView.setAdapter(reviewAdapter);

        selectedMovie = ((DetailActivity)this.getActivity()).getSelectedMovie();

        // Check the instant internet connection status
        merlinsBeard = MerlinsBeard.from(getContext());

        if(getUserVisibleHint()){ // fragment is visible
            loadData();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        selectedMovie = ((DetailActivity)this.getActivity()).getSelectedMovie();
    }

    // Launch the network connection to get the data from the Movie database API
    public void queryReviews() {
        if  (!dataIsLoaded & !noDataFound) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(REVIEW_LOADER_ID, null, this);
            binding.recyclerReviews.recyclerView.setVisibility(View.GONE);
            binding.recyclerReviews.emptyView.setVisibility(View.GONE);
            binding.recyclerReviews.loadingSpinner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public android.support.v4.content.Loader<List<Review>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        return new ReviewLoader(getContext(), reviewsUrlBuilder(selectedMovie.getId()));
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Review>> loader, List<Review> reviews) {
        // Hide loading indicator because the data has been loaded
        binding.recyclerReviews.loadingSpinner.setVisibility(View.GONE);
        // Clear the adapter of previous movies data
        reviewAdapter.setReviewInfoList(null);
        // If there is a valid list of movies, then add them to the adapter's data set.
        // This will trigger the GridView to update itself.
        if (reviews != null && !reviews.isEmpty()) {
            reviewAdapter.setReviewInfoList(reviews);
            reviewAdapter.notifyDataSetChanged();
            // Show the successful loading layout
            binding.recyclerReviews.recyclerView.setVisibility(View.VISIBLE);
            reviewsList = new ArrayList<>(reviews);
            dataIsLoaded = true;
        }
        else {
            // Set empty view to display the "no results found" image
            binding.recyclerReviews.emptyView.setImageResource(R.drawable.no_results);
            binding.recyclerReviews.emptyView.setVisibility(View.VISIBLE);
            noDataFound = true;
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Review>> loader) {
        //Loader reset, so we can clear out our existing data.
        reviewAdapter.setReviewInfoList(null);
    }

    // Check connectivity and take appropriate display action
    private void loadData(){
        if(!merlinsBeard.isConnected()) {
            noInternetDisclaimer();
        } else {
            queryReviews();
        }
    }

    // No internet disclaimer
    private void noInternetDisclaimer(){
        binding.recyclerReviews.recyclerView.setVisibility(View.GONE);
        binding.recyclerReviews.emptyView.setImageResource(R.drawable.no_internet_connection);
        binding.recyclerReviews.emptyView.setVisibility(View.VISIBLE);
    }
}