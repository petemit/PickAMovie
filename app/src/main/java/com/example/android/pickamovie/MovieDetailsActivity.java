package com.example.android.pickamovie;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pickamovie.data.MovieDBContract;
import com.example.android.pickamovie.data.MovieDBHelper;
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
    private final boolean MOVIEFAVORITED=true;
    private final boolean MOVIEUNFAVORITED=false;
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





//Set up the floating action button to show the correct toggle whether it's been favorited or not
        //need to get a cursor object to get the data first:

        //this will query based off of the movies api id
        Uri queryUri = MovieDBContract.buildDeleteQueryByAPIID(Long.parseLong(md.getId()));

        Cursor curs = getContentResolver().query(queryUri, null, null, null, null);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFabIcon(fab, view.getContext());
            }
        });

        if (curs.moveToFirst()) {
            fab.setTag(MOVIEFAVORITED);
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_full_star));

            md.setDbID(curs.getLong(curs.getColumnIndex(MovieDBContract.FavoriteMovies._ID)));
        }




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void switchFabIcon(FloatingActionButton fab, Context context){
        //will return true if it has been favorited.  This will allow us to toggle.
        if (!(fab.getTag ()==null)&& (boolean)fab.getTag()){
            fab.setTag(MOVIEUNFAVORITED);
            fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_empty_star));
            //need content Provider logic
            if (md.getDbID()>-1){
                Uri deleteUri=MovieDBContract.buildSingleFavoriteQueryUri(md.getDbID());
                getContentResolver().delete(deleteUri,null,null);
            }

        }
        else {
            fab.setTag(MOVIEFAVORITED);
            fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_full_star));
            //Need Content Provider Logic

            ContentValues cv = new ContentValues();
            cv.put(MovieDBContract.FavoriteMovies.COLUMN_API_ID,md.getId());
            cv.put(MovieDBContract.FavoriteMovies.COLUMN_OVERVIEW,md.getOverview());
            cv.put(MovieDBContract.FavoriteMovies.COLUMN_POSTER_PATH,md.getPoster_path());
            cv.put(MovieDBContract.FavoriteMovies.COLUMN_RELEASE_DATE,md.getRelease_date());
            cv.put(MovieDBContract.FavoriteMovies.COLUMN_ORIGINAL_TITLE,md.getOriginal_title());
            cv.put(MovieDBContract.FavoriteMovies.COLUMN_TITLE,md.getTitle());
            cv.put(MovieDBContract.FavoriteMovies.COLUMN_POPULARITY,md.getPopularity());
            cv.put(MovieDBContract.FavoriteMovies.COLUMN_VOTE_COUNT,md.getVote_count());
            cv.put(MovieDBContract.FavoriteMovies.COLUMN_VOTE_AVERAGE,md.getVote_average());

         md.setDbID(ContentUris.parseId(getContentResolver().insert(MovieDBContract.FavoriteMovies.CONTENT_URI,cv)));
        }
    }

}
