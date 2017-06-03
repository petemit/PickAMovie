package com.example.android.pickamovie;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pickamovie.data.MovieDBContract;
import com.example.android.pickamovie.data.MovieDBHelper;
import com.example.android.pickamovie.utils.MovieRVAdapter;
import com.example.android.pickamovie.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.Inflater;

import static android.net.Uri.parse;

public class MovieDetailsActivity extends AppCompatActivity {
private MovieData md;
    private ImageView iv;
    private TextView title;
    private TextView synopsis;
    private TextView voteAverage;
    private TextView releaseDate;
    private ViewGroup reviewroot;
    private ViewGroup videoroot;
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

        reviewroot=(ViewGroup)findViewById(R.id.review_root);
        videoroot=(ViewGroup)findViewById(R.id.video_root);

        new getMovieReviews().execute(md.getId());
        new getMovieVideos().execute(md.getId());





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


    public class getMovieReviews extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {

            URL reviewurl = NetworkUtils.reviewsUrlBuilder(params[0]);

            String reviewResponse = "";
            try {


                reviewResponse = NetworkUtils.getResponseFromURL(reviewurl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject reviewjo = null;
            try {

                reviewjo = new JSONObject(reviewResponse);

            } catch (JSONException j) {
                j.printStackTrace();
            }
            return reviewjo;
        }

        @Override
        protected void onPostExecute(JSONObject jo) {
            LayoutInflater inflater =  getLayoutInflater();
            super.onPostExecute(jo);
            ArrayList<String> reviewList = new ArrayList<String>();
            JSONArray ja = null;
            boolean canContinue = true;
            try {
                ja = jo.getJSONArray("results");

                for (int i = 0; i < ja.length(); i++) {
                    View view =inflater.inflate(R.layout.review_item, reviewroot, false);
                    TextView tv_author = (TextView) view.findViewById(R.id.tv_author);
                    TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
                    TextView tv_url = (TextView) view.findViewById(R.id.tv_content_more);
                    String author = ja.getJSONObject(i).get("author").toString();
                    String content = ja.getJSONObject(i).get("content").toString();
                    if (content.length() >101) {
                        content = content.substring(0, 100) + ". . .";
                    }
                    else{
                        tv_url.setVisibility(View.GONE);
                    }
                    final String link = ja.getJSONObject(i).get("url").toString();
                    tv_author.setText(author);
                    tv_content.setText(content);
                    tv_url.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                            if (myIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(myIntent);
                            }
                        }
                    });
                    reviewroot.addView(view);
                    reviewroot.refreshDrawableState();
//
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                canContinue = false;


            }
            if (canContinue) {

                Log.i("MainMovieActivity", "got data");
            }

        }
//
//        private void noInternetConnectionNotify() {
//            loadingText.setText(R.string.noInternet);
//            loadingText.setVisibility(View.VISIBLE);
//            loadingPB.setVisibility(View.INVISIBLE);
//        }

    }


    public class getMovieVideos extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {

            URL videourl = NetworkUtils.videoUrlBuilder(params[0]);

            String videoResponse = "";
            try {


                videoResponse = NetworkUtils.getResponseFromURL(videourl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject videojo = null;
            try {

                videojo = new JSONObject(videoResponse);

            } catch (JSONException j) {
                j.printStackTrace();
            }
            return videojo;
        }

        @Override
        protected void onPostExecute(JSONObject jo) {
            LayoutInflater inflater =  getLayoutInflater();
            super.onPostExecute(jo);

            JSONArray ja = null;
            boolean canContinue = true;
            try {
                ja = jo.getJSONArray("results");

                for (int i = 0; i < ja.length(); i++) {
                    View view =inflater.inflate(R.layout.video_item, videoroot, false);
                    ImageView movieThumbnail= (ImageView) view.findViewById(R.id.tv_movie_thumbnail);
                    TextView tv_Title = (TextView) view.findViewById(R.id.tv_trailer_title);

                    String trailername = ja.getJSONObject(i).get("name").toString();
                    final String key = ja.getJSONObject(i).get("key").toString();

                    Uri thumbnailUri= NetworkUtils.youtubeThumbnailGrab(key);
                    Picasso.with(view.getContext()).load(thumbnailUri).into(movieThumbnail);
                    tv_Title.setText(trailername);

                    movieThumbnail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+key));
                            if (myIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(myIntent);
                            }
                            else{
                                Intent vidWebIntent = new Intent(Intent.ACTION_VIEW, NetworkUtils.youtubeLinkBuilder(key));
                                if (vidWebIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(vidWebIntent);
                                }

                            };

                        }
                    });
                    videoroot.addView(view);


//
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                canContinue = false;


            }
            if (canContinue) {

                Log.i("MainMovieActivity", "got data");
            }

        }
//
//        private void noInternetConnectionNotify() {
//            loadingText.setText(R.string.noInternet);
//            loadingText.setVisibility(View.VISIBLE);
//            loadingPB.setVisibility(View.INVISIBLE);
//        }

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
