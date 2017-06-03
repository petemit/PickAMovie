package com.example.android.pickamovie.utils;


import android.net.Uri;

import com.example.android.pickamovie.MainMovieActivity;
import com.example.android.pickamovie.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Peter on 4/25/2017.
 */

public class NetworkUtils {

    //todo Remove API key before putting in GIT!
    private static final String APIKEY="";
    private static final String baseImageURI="image.tmdb.org";
    private static final String baseURI="api.themoviedb.org";
    private static final String imagepath1="t";
    private static final String imagepath2="p";
    private static final String apiversion3 ="3";
    private static final String moviepath="movie";
    private static final String videoPath="videos";
    private static final String reviewPath="reviews";
    private static final String apiKeyParameter="api_key";
    private static final String imageSize="w185";
    private static final String baseYoutubeImageUri="img.youtube.com";
    private static final String youtubePath1="vi";
    private static final String finalYoutubePath="0.jpg";

    private static final String baseYoutubeVideoUri="youtube.com";
    private static final String youtubeVidPath1="v";
    private static final String youtubeWatchParameter="watch";

    public static URL movieUrlBuilder(String s){
        Uri.Builder builder = new Uri.Builder();
        Uri uri=builder.scheme("http")
                .authority(baseURI)
                .appendPath(apiversion3)
                .appendPath(moviepath)
                .appendPath(s)
                .appendQueryParameter(apiKeyParameter,APIKEY).build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL videoUrlBuilder(String s){
        Uri.Builder builder = new Uri.Builder();
        Uri uri=builder.scheme("http")
                .authority(baseURI)
                .appendPath(apiversion3)
                .appendPath(moviepath)
                .appendPath(s)
                .appendPath(videoPath)
                .appendQueryParameter(apiKeyParameter,APIKEY).build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static Uri youtubeLinkBuilder(String s){
        Uri.Builder builder = new Uri.Builder();
        Uri uri=builder.scheme("http")
                .authority(baseYoutubeVideoUri)
                .appendPath(youtubeWatchParameter)
                .appendQueryParameter(youtubeVidPath1,s).build();

//        URL url = null;
//        try {
//            url = new URL(uri.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        return url;
        return uri;

    }

    public static URL reviewsUrlBuilder(String s){
        Uri.Builder builder = new Uri.Builder();
        Uri uri=builder.scheme("http")
                .authority(baseURI)
                .appendPath(apiversion3)
                .appendPath(moviepath)
                .appendPath(s)
                .appendPath(reviewPath)
                .appendQueryParameter(apiKeyParameter,APIKEY).build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL imageUrlBuilder(String posterpath){
        //todo maybe allow tablets by making imagesize dynamic

        Uri.Builder builder = new Uri.Builder();
        Uri uri=builder.scheme("http")
                .authority(baseImageURI)
                .appendPath(imagepath1)
                .appendPath(imagepath2)
                .appendPath(imageSize)
                //had to put in encoded path because of the URL
                .appendEncodedPath(posterpath).build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }


    public static Uri youtubeThumbnailGrab(String id){
        //todo maybe allow tablets by making imagesize dynamic

        Uri.Builder builder = new Uri.Builder();
        Uri uri=builder.scheme("http")
                .authority(baseYoutubeImageUri)
                .appendPath(youtubePath1)
                .appendPath(id)
                .appendPath(finalYoutubePath)
                .build();
        URL url = null;
//        try {
//            url = new URL(uri.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

        return uri;

    }

    public static String getResponseFromURL(URL url)throws  IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            byte[] buffer = new byte[1024];
            int length;
            InputStream in = conn.getInputStream();
            while ((length= in.read(buffer))!=-1){
                result.write(buffer,0,length);
            }
          //InputStream in = new BufferedInputStream(conn.getInputStream());
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line);
//
//            }
        }
        finally {
            conn.disconnect();
        }

    //return sb.toString();
        return result.toString();

    }

    //This class is from the udacity course.  I'm keeping it here in case I get stuck.
//    public static String getResponseFromHttpUrl(URL url) throws IOException {
//        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        try {
//            InputStream in = urlConnection.getInputStream();
//
//            Scanner scanner = new Scanner(in);
//            scanner.useDelimiter("\\A");
//
//            boolean hasInput = scanner.hasNext();
//            if (hasInput) {
//                return scanner.next();
//            } else {
//                return null;
//            }
//        } finally {
//            urlConnection.disconnect();
//        }
//    }






}
