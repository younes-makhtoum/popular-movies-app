package com.example.android.popularmovies.ui.detailsscreens;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.databinding.FragmentSummaryBinding;
import com.example.android.popularmovies.ui.welcomescreen.Movie;
import com.squareup.picasso.Picasso;

import static com.example.android.popularmovies.services.UrlBuilder.movieThumbnailUrlBuilder;
import static com.example.android.popularmovies.services.UrlBuilder.movieUrlBuilder;

public class SummaryFragment extends Fragment {

    // Tag for log messages
    public static final String LOG_TAG = SummaryFragment.class.getName();

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
                .load(movieThumbnailUrlBuilder(selectedMovie.getPoster()))
                .placeholder(R.drawable.thumbnail_place_holder)
                .into(binding.moviePosterDetail);

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

        populateUI();

        return rootView;
    }

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
