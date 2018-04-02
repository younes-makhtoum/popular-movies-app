package com.example.android.popularmovies.ui.detailsscreens;

import android.support.design.widget.TabLayout;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ToxicBakery.viewpager.transforms.ScaleInOutTransformer;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.ui.detailsscreens.reviews.ReviewsFragment;
import com.example.android.popularmovies.ui.detailsscreens.trailers.TrailersFragment;
import com.example.android.popularmovies.ui.welcomescreen.Movie;
import com.example.android.popularmovies.ui.welcomescreen.SettingsActivity;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.registerable.connection.Connectable;

public class DetailActivity extends AppCompatActivity {

    // Tag for log messages
    private static final String LOG_TAG = DetailActivity.class.getName();

    // Movie object instance declaration to handle the received parcelable
    private Movie selectedMovie;

    // Used to check the internet connection changes
    Merlin merlin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the content view (replacing `setContentView`)
        setContentView(R.layout.activity_detail);

        // Collect our intent and get our parcel with the selected Movie object
        Intent intent = getIntent();
        selectedMovie  = intent.getParcelableExtra("Movie");

        setTitle("");

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setId(R.id.view_pager);

        // Create an adapter that knows which fragment should be shown on each page
        DetailAdapter detailAdapter = new DetailAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(detailAdapter);

        // Animate the ViewPager
        viewPager.setPageTransformer(true, new ScaleInOutTransformer());

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        merlin = new Merlin.Builder().withConnectableCallbacks().build(this);

        // Internet status activation is monitored with this listener registration
        merlin.registerConnectable(new Connectable() {
            @Override
            public void onConnect() {

                TrailersFragment trailersFragment = (TrailersFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 1);
                ReviewsFragment reviewsFragment = (ReviewsFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 2);

                if (trailersFragment.isAdded()) {
                    trailersFragment.queryTrailers();
                }
                if (reviewsFragment != null) {
                    if (reviewsFragment.isAdded()) {
                    Log.v(LOG_TAG,"LOG// reviewsFragment isAdded");
                    reviewsFragment.queryReviews();
                    }
                }
            }
        });
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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Movie getSelectedMovie(){
        return this.selectedMovie;
    }
}