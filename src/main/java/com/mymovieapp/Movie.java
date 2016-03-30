package com.mymovieapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Honey on 3/29/2016.
 */
public class Movie implements Parcelable, Comparable<com.mymovieapp.Movie> {

    String name;
    String date;
    String photoId;
    String synopsis;
    String ratingRuntime;
    Rating rating;

    public Movie(String name, String date, String photoId, String synopsis, String ratingRuntime, Rating rating) {
        this.name = name;
        this.date = date;
        this.photoId = photoId;
        this.synopsis = synopsis;
        this.ratingRuntime = ratingRuntime;
        this.rating = rating;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(date);
        out.writeString(photoId);
        out.writeString(synopsis);
        out.writeString(ratingRuntime);
        out.writeParcelable(rating, flags);
    }

    public static final Parcelable.Creator<com.mymovieapp.Movie> CREATOR
            = new Parcelable.Creator<com.mymovieapp.Movie>() {
        public com.mymovieapp.Movie createFromParcel(Parcel in) {
            return new com.mymovieapp.Movie(in);
        }

        public com.mymovieapp.Movie[] newArray(int size) {
            return new com.mymovieapp.Movie[size];
        }
    };

    private Movie(Parcel in) {
        name = in.readString();
        date = in.readString();
        photoId = in.readString();
        synopsis = in.readString();
        ratingRuntime = in.readString();
        rating = in.readParcelable(Rating.class.getClassLoader());
    }

    //Override
    public int compareTo(com.mymovieapp.Movie o) {
        return this.rating.compareTo(o.getRating());
    }

    public boolean equals(Object o) {
        return (o instanceof com.mymovieapp.Movie)
                && (this.getName().equals(((com.mymovieapp.Movie) o).getName()));
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

    public String getRatingRuntime() { return ratingRuntime; }

    public Rating getRating() {
        return rating;
    }


}
