package com.example.android.popularmovies.ui.detailsscreens;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.popularmovies.ui.detailsscreens.reviews.ReviewsFragment;
import com.example.android.popularmovies.ui.detailsscreens.trailers.TrailersFragment;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class DetailAdapter extends FragmentPagerAdapter {

    // Tag for log messages
    public static final String LOG_TAG = DetailAdapter.class.getName();

    private String tabTitles[] = new String[] {"Summary", "Trailers", "Reviews"};

    public DetailAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SummaryFragment();
            case 1:
                return new TrailersFragment();
            case 2:
                return new ReviewsFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return 3;
    }
  }
