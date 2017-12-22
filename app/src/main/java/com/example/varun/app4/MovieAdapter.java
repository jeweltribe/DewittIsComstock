package com.example.varun.app4;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;



public class MovieAdapter extends ArrayAdapter<Movies> {
    private final int mLayoutResourceId;
    private final List<Movies> mData;
    private final Context context;

    public MovieAdapter(Context context, List<Movies> lst) {
        super(context, R.layout.row, lst);
        this.mLayoutResourceId = R.layout.row;
        this.mData = lst;
        this.context = context;
    }

    // holder class for the items in the listview row
    public static class ViewHolder {
        public TextView mName;
        public TextView mDate;
        public RatingBar mRating;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;

        if (v == null) {
            LayoutInflater lf = LayoutInflater.from(context);
            v = lf.inflate(mLayoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.mName = (TextView) v.findViewById(R.id.movieName);
            holder.mDate = (TextView) v.findViewById(R.id.movieDate);
            holder.mRating = (RatingBar) v.findViewById(R.id.ratingBar);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag(); // recycle listview item
        }
        Movies movie = mData.get(position);
        holder.mName.setText(movie.getMovieName());
        holder.mDate.setText(movie.getMovieDate());
        holder.mRating.setRating(movie.getMovieRating());
        return v;
    }
}
