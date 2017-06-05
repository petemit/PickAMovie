package com.example.android.pickamovie.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.pickamovie.MainMovieActivity;

/**
 * Created by Peter on 5/30/2017.
 */

public class MovieDBContentProvider extends ContentProvider {
    private MovieDBHelper dbHelper;


    public static final int FAVORITES = 100;
    public static final int FAVORITE_BY_ID = 200;
    public static final int FAVORITE_BY_APIID = 201;

    public static final UriMatcher uriMatcher = makeUriMatcher();

    public static final String[] FAVORITE_LIST={MovieDBContract.FavoriteMovies._ID,
    MovieDBContract.FavoriteMovies.COLUMN_POSTER_PATH,
            MovieDBContract.FavoriteMovies.COLUMN_TITLE};

    public static UriMatcher makeUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieDBContract.AUTHORITY,
                MovieDBContract.FavoriteMovies.TABLE_NAME, FAVORITES);
        matcher.addURI(MovieDBContract.AUTHORITY,
                MovieDBContract.FavoriteMovies.TABLE_NAME + "/#", FAVORITE_BY_ID);
        matcher.addURI(MovieDBContract.AUTHORITY,
                MovieDBContract.FavoriteMovies.TABLE_NAME + "/" +
                MovieDBContract.API_PATH + "/#", FAVORITE_BY_APIID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper=new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Cursor cursor=null;
        SQLiteDatabase db= dbHelper.getReadableDatabase();






        switch (uriMatcher.match(uri)){
            case FAVORITES:
                cursor= db.query(MovieDBContract.FavoriteMovies.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
                break;
            case FAVORITE_BY_ID:
                String mSelection= MovieDBContract.FavoriteMovies._ID + "=? ";
                String[] mSelectionArgs= {String.valueOf(ContentUris.parseId(uri))};
                cursor= db.query(MovieDBContract.FavoriteMovies.TABLE_NAME,
                        null,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        null);
                break;

            case FAVORITE_BY_APIID:
                String mAPISelection= MovieDBContract.FavoriteMovies.COLUMN_API_ID + "=? ";
                String[] mAPISelectionArgs= {String.valueOf(ContentUris.parseId(uri))};
                cursor= db.query(MovieDBContract.FavoriteMovies.TABLE_NAME,
                        null,
                        mAPISelection,
                        mAPISelectionArgs,
                        null,
                        null,
                        null);
                break;
            default:
                throw new IllegalArgumentException("Query not supported");
        }
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case FAVORITES:
                long _id = db.insert(MovieDBContract.FavoriteMovies.TABLE_NAME,
                        null,
                        values);
                if (_id >0){

                   return MovieDBContract.buildSingleFavoriteQueryUri(_id);

                }
                else{
                    throw new SQLException("Insert failed!");
                }

            default:
                throw new UnsupportedOperationException("that insert query is not supported, man.");

        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case FAVORITE_BY_ID:

                String[] _id={String.valueOf(ContentUris.parseId(uri))};
                if (_id.length==0){
                    return 0;
                }

                String mSelection= MovieDBContract.FavoriteMovies._ID+"=?";

                int rowsdeleted = db.delete(MovieDBContract.FavoriteMovies.TABLE_NAME,
                        mSelection,
                        _id);
                if (_id.length >0){

                    return rowsdeleted;

                }
                else{
                    throw new SQLException("Delete failed!");
                }


            case FAVORITE_BY_APIID:
                String[] _APIid={String.valueOf(ContentUris.parseId(uri))};
                if (_APIid.length==0){
                    return 0;
                }

                String mAPISelection= MovieDBContract.FavoriteMovies.COLUMN_API_ID+"=?";

                int rowsdeletedAPI = db.delete(MovieDBContract.FavoriteMovies.TABLE_NAME,
                        mAPISelection,
                        _APIid);
                if (_APIid.length >0){

                    return rowsdeletedAPI;

                }
                else{
                    throw new SQLException("Delete failed!");
                }
            default:
                throw new UnsupportedOperationException("that delete query is not supported, man.");

        }

    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }
}
