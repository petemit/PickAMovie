package com.example.android.pickamovie.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Network;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pickamovie.MainMovieActivity;
import com.example.android.pickamovie.MovieData;
import com.example.android.pickamovie.MovieDetailsActivity;
import com.example.android.pickamovie.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Peter on 4/22/2017.
 */

public class MovieRVAdapter extends RecyclerView.Adapter<MovieRVAdapter.MovieRVAdapterViewHolder> {

    private int itemcount;
    private ArrayList<MovieData> movies;
    Context context;

    public MovieRVAdapter(ArrayList movies) {
        this.movies = movies;
    }

    @Override
    public MovieRVAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.movie_item, parent, false);

        MovieRVAdapterViewHolder vh = new MovieRVAdapterViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MovieRVAdapterViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieRVAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv;
        ImageView iv;
        MovieData md;

        public MovieRVAdapterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tv = (TextView) itemView.findViewById(R.id.tv_movie_test_text);
            iv = (ImageView) itemView.findViewById(R.id.iv_movie_poster);


        }

        void bind(MovieData md) {
            this.md = md;
            tv.setText(md.getTitle());
            URL url = NetworkUtils.imageUrlBuilder(md.getPoster_path());
            Picasso.with(itemView.getContext()).load(url.toString()).into(iv);
            //This is where all the magic will happen.


        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("movieData", md);
            intent.setClass(v.getContext(), MovieDetailsActivity.class);
            if (intent.resolveActivity(v.getContext().getPackageManager())!=null) {
                v.getContext().startActivity(intent);
            }

        }
    }

}