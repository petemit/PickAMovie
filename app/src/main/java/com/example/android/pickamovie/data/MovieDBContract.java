package com.example.android.pickamovie.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Peter on 5/30/2017.
 */

public class MovieDBContract {

    public static final String AUTHORITY="com.example.android.pickamovie";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+AUTHORITY);
    public static final String API_PATH="API";



    public static class FavoriteMovies implements BaseColumns {



        public static String TABLE_NAME = "favorite_movies";
        public static String COLUMN_API_ID="api_id";
        public static String COLUMN_OVERVIEW="overview";
        public static String COLUMN_POSTER_PATH="poster_path";
        public static String COLUMN_RELEASE_DATE="release_date";
        public static String COLUMN_ORIGINAL_TITLE="original_title";
        public static String COLUMN_TITLE="title";
        public static String COLUMN_POPULARITY="popularity";
        public static String COLUMN_VOTE_COUNT="vote_count";
        public static String COLUMN_VOTE_AVERAGE="vote_average";


        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().
                appendPath(TABLE_NAME).build();
    }

    public static Uri buildSingleFavoriteQueryUri(long id) {
        return ContentUris.withAppendedId(FavoriteMovies.CONTENT_URI,id);
    }

    public static Uri buildDeleteQueryByAPIID(long id) {
        return ContentUris.withAppendedId(FavoriteMovies.CONTENT_URI.
                buildUpon().appendPath(API_PATH).build(),id);
    }
}
