package com.mymovieapp;

import android.widget.ImageView;

/**
 * Created by Honey on 3/29/2016.
 */
public class Movie {

    String name;
    String date;
    String photoId;
    String synopsis;
    float rating;
    ImageView picture;

    public Movie(String name, String date, String photoId, String synopsis, float rating, ImageView picture) {
        this.name = name;
        this.date = date;
        this.photoId = photoId;
        this.synopsis = synopsis;
        this.rating = rating;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getPhotoID() {
        return photoId;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public float getRating() {
        return rating;
    }
}
