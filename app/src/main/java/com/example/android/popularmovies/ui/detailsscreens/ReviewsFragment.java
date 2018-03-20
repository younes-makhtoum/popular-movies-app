package com.example.android.popularmovies.ui.detailsscreens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.databinding.FragmentSummaryBinding;

public class ReviewsFragment extends Fragment {

    // Store the binding
    private FragmentSummaryBinding binding;

    // Declare an instance of Movie
    private Movie selectedMovie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        selectedMovie = ((DetailActivity)this.getActivity()).getSelectedMovie();
    }
}