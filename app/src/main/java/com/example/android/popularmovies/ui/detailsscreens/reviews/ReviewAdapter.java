package com.example.android.popularmovies.ui.detailsscreens.reviews;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    // Tag for log messages
    public static final String LOG_TAG = ReviewAdapter.class.getName();

    private final Context context;
    private List<Review> reviewsList;

    public ReviewAdapter(Context context, List<Review> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView reviewContentTextView;
        private TextView reviewAuthorTextView;

        private MyViewHolder(View itemView) {
            super(itemView);
            reviewContentTextView = itemView.findViewById(R.id.review_content);
            reviewAuthorTextView = itemView.findViewById(R.id.review_author);
        }
    }

    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.MyViewHolder holder, final int position) {

        final Review currentReview = reviewsList.get(position);

        holder.reviewContentTextView.setText(currentReview.getContent());
        holder.reviewAuthorTextView.setText(currentReview.getAuthor());

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentReview.getUrl()));
                try {
                    context.startActivity(webIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (reviewsList == null) {
            return 0;
        } else {
            return reviewsList.size();
        }
    }

    // Helper method to set the actual reviews list into the recycler view on the activity
    public void setReviewInfoList(List<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }
}