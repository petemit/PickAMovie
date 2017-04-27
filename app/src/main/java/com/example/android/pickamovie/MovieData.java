package com.example.android.pickamovie;

import java.io.Serializable;

/**
 * Created by Peter on 4/25/2017.
 */

public class MovieData implements Serializable{
    private String overview;
    private String poster_path;
    private String release_date;
    private String id;
    private String original_title;
    private String title;
    private float popularity;
    private int vote_count;
    private float vote_average;

    public MovieData(String overview, String poster_path, String release_date, String id, String original_title, String title, float popularity, int vote_count, float vote_average) {
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.id = id;
        this.original_title = original_title;
        this.title = title;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
    }

    public MovieData(String id){
        this.id=id;
    }
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }
}
