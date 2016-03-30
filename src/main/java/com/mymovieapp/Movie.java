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
    ImageView picture;

    public Movie(String name, String date, String photoId, String synopsis, ImageView picture) {
        this.name = name;
        this.date = date;
        this.photoId = photoId;
        this.synopsis = synopsis;
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
}
