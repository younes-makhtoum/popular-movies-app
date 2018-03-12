package com.example.android.popularmovies.ui.detailsscreen;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.databinding.ActivityDetailBinding;
import com.example.android.popularmovies.services.QueryUtils;
import com.squareup.picasso.Picasso;
import static com.example.android.popularmovies.services.UrlBuilder.urlBuilder;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private static final String LOG_TAG = DetailActivity.class.getName();

    // Store the binding
    private ActivityDetailBinding binding;

    // Declare an instance of Movie
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the content view (replacing `setContentView`)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = 0;

        if (intent != null) {
            position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        }
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        movie = movieProvider(position);

        setTitle(movie.getTitle());

        Picasso.with(this)
                .load(urlBuilder(movie.getPoster()))
                .into(binding.moviePoster);

        populateUI();
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {

        if(!movie.getTitle().isEmpty()) {
            binding.titleData.setText(movie.getTitle());
        }
        else {
            binding.titleLabel.setVisibility(View.GONE);
            binding.titleData.setVisibility(View.GONE);
        }

        if(!movie.getReleaseDate().isEmpty()) {
            binding.releaseDateData.setText(movie.getReleaseDate());
        }
        else {
            binding.releaseDateLabel.setVisibility(View.GONE);
            binding.releaseDateData.setVisibility(View.GONE);
        }

        Log.v(LOG_TAG, String.valueOf(movie.getUserRating()));

        if(!String.valueOf(movie.getUserRating()).isEmpty()) {
            binding.userRatingData.setText(String.valueOf(movie.getUserRating()));
        }
        else {
            binding.userRatingLabel.setVisibility(View.GONE);
            binding.userRatingData.setVisibility(View.GONE);
        }

        if(!movie.getPlotSynopsis().isEmpty()) {
            binding.plotSynopsisData.setText(movie.getPlotSynopsis());
        }
        else {
            binding.plotSynopsisLabel.setVisibility(View.GONE);
            binding.plotSynopsisData.setVisibility(View.GONE);
        }
    }

    private Movie movieProvider(int position) {
        String[] movies = getResources().getStringArray(R.array.movie_details);
        return QueryUtils.parseMovieJson(movies[position]);
    }
}
