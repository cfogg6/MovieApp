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
    String id;
    Rating rating;

    /**
     * instance of movie
     * @param name name of movie
     * @param date date of when movie was released
     * @param photoId picture of movie
     * @param synopsis summary of movie
     * @param ratingRuntime Rating (G, PG, etc) and runtime of movie
     * @param id id that rotten tomatoes distinguishes movies by
     * @param rating average rating considering users' inputs
     */
    public Movie(String name, String date, String photoId, String synopsis, String ratingRuntime, String id, Rating rating) {
        this.name = name;
        this.date = date;
        this.photoId = photoId;
        this.synopsis = synopsis;
        this.ratingRuntime = ratingRuntime;
        this.id = id;
        this.rating = rating;
    }

    /**
     * method required for parcel
     * @return 0
     */
    public int describeContents() {
        return 0;
    }

    /**
     * wrap movie as parcel
     * @param out new parcel
     * @param flags flags
     */
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(date);
        out.writeString(photoId);
        out.writeString(synopsis);
        out.writeString(ratingRuntime);
        out.writeString(id);
        out.writeParcelable(rating, flags);
    }

    public static final Parcelable.Creator<com.mymovieapp.Movie> CREATOR = new Parcelable.Creator<com.mymovieapp.Movie>() {
        public com.mymovieapp.Movie createFromParcel(Parcel in) {
            return new com.mymovieapp.Movie(in);
        }

        public com.mymovieapp.Movie[] newArray(int size) {
            return new com.mymovieapp.Movie[size];
        }
    };

    /**
     * makes new Parcel for movie
     * @param in new Parcel
     */
    private Movie(Parcel in) {
        name = in.readString();
        date = in.readString();
        photoId = in.readString();
        synopsis = in.readString();
        ratingRuntime = in.readString();
        id = in.readString();
        rating = in.readParcelable(Rating.class.getClassLoader());
    }

    /**
     * compares this rating to another rating
     * @param o new rating
     * @return int that describes which rating is higher
     */
    //Override
    public int compareTo(com.mymovieapp.Movie o) {
        return this.rating.compareTo(o.getRating());
    }

    /**
     *compares objects to make sure they're equal
     * @param o new object
     * @return true if movies are similar, false if not
     */
    public boolean equals(Object o) {
        return (o instanceof com.mymovieapp.Movie)
                && (this.getName().equals((((com.mymovieapp.Movie) o).getName())));
    }

    /**
     * gets name
     * @return string name
     */
    public String getName() {
        return name;
    }

    /**
     *gets movie ID
     * @return String id
     */
    public String getId() {
        return id;
    }

    /**
     *gets Date of movie made
     * @return String date
     */
    public String getDate() {
        return date;
    }

    /**
     * gets PhotoId
     * @return String photo iD
     */
    public String getPhotoID() {
        return photoId;
    }

    /**
     * gets synopsis
     * @return String synopsis
     */
    public String getSynopsis() {
        return synopsis;
    }

    /**
     * gets RatingRuntime
     * @return String of rating and runtime
     */
    public String getRatingRuntime() { return ratingRuntime; }

    /**
     * gets avergage rating
     * @return Rating
     */
    public Rating getRating() {
        return rating;
    }
}
