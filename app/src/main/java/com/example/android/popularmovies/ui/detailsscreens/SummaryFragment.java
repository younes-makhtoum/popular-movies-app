package com.example.android.popularmovies.ui.detailsscreens;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.databinding.FragmentSummaryBinding;
import com.squareup.picasso.Picasso;

import static com.example.android.popularmovies.services.UrlBuilder.thumbnailUrlBuilder;

public class SummaryFragment extends Fragment {

    // Store the binding
    private FragmentSummaryBinding binding;

    // Declare an instance of Movie
    private Movie selectedMovie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSummaryBinding.bind(inflater.inflate(R.layout.fragment_summary, container, false));

        View rootView = binding.getRoot();

        selectedMovie = ((DetailActivity)this.getActivity()).getSelectedMovie();

        Picasso.with(getContext())
                .load(thumbnailUrlBuilder(selectedMovie.getPoster()))
                .into(binding.moviePosterDetail);

        populateUI();

        return rootView;
    }

/*    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        selectedMovie = ((DetailActivity)this.getActivity()).getSelectedMovie();
    }*/

    // Populate the summary screen with detailed data about the selected movie.
    private void populateUI() {

        if(!selectedMovie.getTitle().isEmpty()) {
            binding.titleData.setText(selectedMovie.getTitle());
        }
        else {
            binding.titleLabel.setVisibility(View.GONE);
            binding.titleData.setVisibility(View.GONE);
        }

        if(!selectedMovie.getReleaseDate().isEmpty()) {
            binding.releaseDateData.setText(selectedMovie.getReleaseDate());
        }
        else {
            binding.releaseDateLabel.setVisibility(View.GONE);
            binding.releaseDateData.setVisibility(View.GONE);
        }

        if(!String.valueOf(selectedMovie.getUserRating()).isEmpty()) {
            binding.userRatingData.setText(String.valueOf(selectedMovie.getUserRating()));
        }
        else {
            binding.userRatingLabel.setVisibility(View.GONE);
            binding.userRatingData.setVisibility(View.GONE);
        }

        if(!selectedMovie.getPlotSynopsis().isEmpty()) {
            binding.plotSynopsisData.setText(selectedMovie.getPlotSynopsis());
        }
        else {
            binding.plotSynopsisLabel.setVisibility(View.GONE);
            binding.plotSynopsisData.setVisibility(View.GONE);
        }
    }
}
