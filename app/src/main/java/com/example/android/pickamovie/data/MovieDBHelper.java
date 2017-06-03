package com.example.android.pickamovie.data;

import android.content.Context;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Peter on 5/30/2017.
 */

public class MovieDBHelper extends SQLiteOpenHelper{
    public static int DB_VERSION=1;
    public static String DB_NAME="localMovieDB.db";

    public MovieDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }





    @Override
    public void onCreate(SQLiteDatabase db) {

        final String createFavMovieSQL = "CREATE TABLE " +
                MovieDBContract.FavoriteMovies.TABLE_NAME+ "(" + MovieDBContract.FavoriteMovies._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieDBContract.FavoriteMovies.COLUMN_API_ID + " TEXT NOT NULL, " +
                MovieDBContract.FavoriteMovies.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MovieDBContract.FavoriteMovies.COLUMN_OVERVIEW + " TEXT, " +
                MovieDBContract.FavoriteMovies.COLUMN_POPULARITY + " FLOAT, " +
                MovieDBContract.FavoriteMovies.COLUMN_POSTER_PATH + " TEXT, " +
                MovieDBContract.FavoriteMovies.COLUMN_TITLE + " TEXT, " +
                MovieDBContract.FavoriteMovies.COLUMN_RELEASE_DATE + " TIMESTAMP, " +
                MovieDBContract.FavoriteMovies.COLUMN_VOTE_AVERAGE + " FLOAT, " +
                MovieDBContract.FavoriteMovies.COLUMN_VOTE_COUNT + " INTEGER);";

        db.execSQL(createFavMovieSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //todo apply persistence for favorites
        db.execSQL("DROP TABLE IF EXISTS " + MovieDBContract.FavoriteMovies.TABLE_NAME);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MovieDBContract.FavoriteMovies.TABLE_NAME + "'");
        onCreate(db);
    }
}
