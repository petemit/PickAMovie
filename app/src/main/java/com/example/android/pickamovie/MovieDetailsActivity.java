package com.example.android.pickamovie;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pickamovie.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;

public class MovieDetailsActivity extends AppCompatActivity {
private MovieData md;
    private ImageView iv;
    private TextView title;
    private TextView synopsis;
    private TextView voteAverage;
    private TextView releaseDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getIntent().hasExtra("movieData")){
            md=(MovieData)getIntent().getSerializableExtra("movieData");
        }
        iv=(ImageView)findViewById(R.id.iv_movie_details_poster);
        title=(TextView)findViewById(R.id.tv_movie_details_title);
        synopsis=(TextView)findViewById(R.id.tv_movie_details_synopsis);
        voteAverage=(TextView)findViewById(R.id.tv_movie_details_vote_average);
        releaseDate=(TextView)findViewById(R.id.tv_movie_details_release_date);
        URL posterPath=NetworkUtils.imageUrlBuilder(md.getPoster_path());
        Picasso.with(getBaseContext()).load(posterPath.toString()).into(iv);
        title.setText(md.getTitle());
        synopsis.setText(md.getOverview());
        voteAverage.setText(String.valueOf(md.getVote_average()));
        releaseDate.setText(md.getRelease_date());




        //no fab for me please
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
