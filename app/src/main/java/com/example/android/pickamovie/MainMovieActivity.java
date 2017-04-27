package com.example.android.pickamovie;

import android.content.Context;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.pickamovie.utils.MovieRVAdapter;
import com.example.android.pickamovie.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainMovieActivity extends AppCompatActivity {

    private RecyclerView rv;
    private TextView loadingText;
    private ProgressBar loadingPB;
    //How many columns are in the grid layout manager
    private final int GRIDLAYOUTCOLUMNCOUNT=2;
    private static final String popularString="popular";
    private static final String topRatedString="top_rated";
    public TextView filterText;
    private String popularFilterLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context =this.getBaseContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rv=(RecyclerView)findViewById(R.id.rv_mainMovie);
        loadingText=(TextView)findViewById(R.id.tv_text_loading);
        loadingPB=(ProgressBar)findViewById(R.id.pb_loading_progressbar);
        filterText=(TextView)findViewById(R.id.tv_current_filter);
        popularFilterLabel=getString(R.string.mostPopularStringLabel);


        //Need to get the length of the array here so the grid can be laid out


        //Create the Layout Manager and set the layout manager for the RecyclerView
        RecyclerView.LayoutManager gridLayoutMgr=new GridLayoutManager(context,GRIDLAYOUTCOLUMNCOUNT);

        rv.setHasFixedSize(true);

        rv.setLayoutManager(gridLayoutMgr);

        new getMovieData().execute(popularString);

        filterText.setText(popularFilterLabel);



    }
            //this is the async data portion that will call the class NetworkUtils for the net-work.  ba doom psh
        public class getMovieData extends AsyncTask<String, Void, JSONObject>{

                @Override
                protected JSONObject doInBackground(String... params) {
                    URL url =NetworkUtils.movieUrlBuilder(params[0]);
                    String response="";
                    try {
                        response=NetworkUtils.getResponseFromURL(url);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                    JSONObject jo=null;
                    try {
                         jo = new JSONObject(response);

                    }
                    catch (JSONException j){
                        j.printStackTrace();
                    }
                return jo;
                }

                @Override
                protected void onPostExecute(JSONObject jo) {
                    super.onPostExecute(jo);
                    ArrayList<MovieData> movieDataArrayList = new ArrayList<MovieData>();
                    JSONArray ja = null;
                    boolean canContinue=true;
                    try {
                        ja = jo.getJSONArray("results");

                        for (int i = 0; i < ja.length(); i++) {

                            String overview=ja.getJSONObject(i).get("overview").toString();
                            String poster_path=ja.getJSONObject(i).get("poster_path").toString();
                            String release_date=ja.getJSONObject(i).get("release_date").toString();
                            String id=ja.getJSONObject(i).get("id").toString();
                            String original_title=ja.getJSONObject(i).get("original_title").toString();
                            String title=ja.getJSONObject(i).get("title").toString();
                            String popularity=ja.getJSONObject(i).get("popularity").toString();
                            String vote_count=ja.getJSONObject(i).get("vote_count").toString();
                            String vote_average=ja.getJSONObject(i).get("vote_average").toString();
                            MovieData md= new MovieData(overview,poster_path,release_date
                            ,id,original_title,title,Float.parseFloat(popularity),Integer.parseInt(vote_count),Float.parseFloat(vote_average));
                            movieDataArrayList.add(md);
                        }
                    }
                     catch (JSONException e) {
                        e.printStackTrace();
                    }
                    catch (NullPointerException e){
                        canContinue=false;
                        noInternetConnectionNotify();

                    }
                    if(canContinue) {
                        displayMovieData(movieDataArrayList);
                        Log.i("MainMovieActivity", "got data");
                    }

                }

                private void noInternetConnectionNotify() {
                    loadingText.setText("No Internet Connection");
                    loadingText.setVisibility(View.VISIBLE);
                    loadingPB.setVisibility(View.INVISIBLE);
                }

                private void displayMovieData(ArrayList<MovieData> movieDataArrayList) {
                    MovieRVAdapter adapter = new MovieRVAdapter(movieDataArrayList);
                    rv.setAdapter(adapter);
                    rv.setVisibility(View.VISIBLE);
                    loadingPB.setVisibility(View.INVISIBLE);
                    loadingText.setVisibility(View.INVISIBLE);
                    filterText.setVisibility(View.VISIBLE);

                }
            }


        //TODO change the default color scheme

        //TODO Connect an item in the recycler view to launch an activity

        //todo make a recyclerview

        //todo make a recyclerview adapterclass



        //Don't want the fab for now

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}