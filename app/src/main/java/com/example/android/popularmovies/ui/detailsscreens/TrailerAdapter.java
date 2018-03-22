package com.example.android.popularmovies.ui.detailsscreens;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.services.UrlBuilder.trailerThumbnailUrlBuilder;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {

    // Tag for log messages
    public static final String LOG_TAG = com.example.android.popularmovies.ui.detailsscreens.TrailerAdapter.class.getName();

    private final Context context;
    private List<Trailer> trailersList = new ArrayList<>();

    public TrailerAdapter(Context context, List<Trailer> trailersList) {
        this.context = context;
        this.trailersList = trailersList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView trailerImageView;
        private TextView trailerNameView;

        private MyViewHolder(View itemView) {
            super(itemView);
            trailerImageView = itemView.findViewById(R.id.trailer_thumbnail);
            trailerNameView = itemView.findViewById(R.id.trailer_name);
        }
    }

    @Override
    public TrailerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.MyViewHolder holder, final int position) {

        final Trailer currentTrailer = trailersList.get(position);

        holder.trailerNameView.setText(currentTrailer.getName());

        Picasso.with(context)
                .load(trailerThumbnailUrlBuilder(currentTrailer.getKey()))
                .into(holder.trailerImageView);

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + currentTrailer.getKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + currentTrailer.getKey()));
                try {
                    context.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(webIntent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (trailersList == null) {
            return 0;
        } else {
            return trailersList.size();
        }
    }

    // Helper method to set the actual trailers list into the recycler view on the activity
    public void setTrailerInfoList(List<Trailer> trailersList) {
        this.trailersList = trailersList;
    }
}