package com.example.android.popularmovies.ui.detailsscreen;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

import static com.example.android.popularmovies.services.UrlBuilder.posterUrlBuilder;

public class DetailActivity extends AppCompatActivity {

    // Tag for log messages
    private static final String LOG_TAG = DetailActivity.class.getName();

    // Store the binding
    private ActivityDetailBinding binding;

    // Declare an instance of Movie
    private Movie selectedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the content view (replacing `setContentView`)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        // Collect our intent and get our parcel with the selected Movie object
        Intent intent = getIntent();
        selectedMovie  = intent.getParcelableExtra("Movie");

        setTitle(selectedMovie.getTitle());

        Picasso.with(this)
                .load(posterUrlBuilder(selectedMovie.getPoster()))
                .into(binding.moviePoster);

        // Populate the UI using this method
        populateUI();
    }

    // Populate the detail screen with the data of the selected movie.
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
