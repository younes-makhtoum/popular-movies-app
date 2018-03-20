package com.example.android.popularmovies.ui.detailsscreens;

import android.support.design.widget.TabLayout;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.popularmovies.R;

public class DetailActivity extends AppCompatActivity {

    // Tag for log messages
    private static final String LOG_TAG = DetailActivity.class.getName();

    // Declare an instance of Movie
    private Movie selectedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the content view (replacing `setContentView`)
        setContentView(R.layout.activity_detail);

        // Collect our intent and get our parcel with the selected Movie object
        Intent intent = getIntent();
        selectedMovie  = intent.getParcelableExtra("Movie");

        setTitle(selectedMovie.getTitle());

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = findViewById(R.id.view_pager);

        // Create an adapter that knows which fragment should be shown on each page
        DetailAdapter detailAdapter = new DetailAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(detailAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public Movie getSelectedMovie(){
        return this.selectedMovie;
    }
}
