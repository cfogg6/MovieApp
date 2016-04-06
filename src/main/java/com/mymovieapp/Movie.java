package com.mymovieapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable, Comparable<com.mymovieapp.Movie> {

    /**
     * Title of movie
     */
    private String name;
    /**
     * Date of movie
     */
    private String date;
    /**
     * Photo URL of movie
     */
    private String photoId;
    /**
     * Synopsis of movie
     */
    private String synopsis;
    /**
     * MPAA rating and runtime of movie
     */
    private String ratingRuntime;
    /**
     * ID of movie
     */
    private String id;
    /**
     * Rating of movie
     */
    private Rating rating;

    /**
     * instance of movie
     * @param name name of movie
     * @param date date of when movie was released
     * @param picture picture of movie
     * @param summary summary of movie
     * @param ratingRuntime Rating (G, PG, etc) and runtime of movie
     * @param id id that rotten tomatoes distinguishes movies by
     * @param avRating average rating considering users' inputs
     */
    public Movie(String name, String date, String picture, String summary, String ratingRuntime, String id, Rating avRating) {
        this.name = name;
        this.date = date;
        this.photoId = picture;
        this.synopsis = summary;
        this.ratingRuntime = ratingRuntime;
        this.id = id;
        this.rating = avRating;
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

    /**
     * Create Parcelable
     */
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
        if (!(o instanceof Movie)) {
            return false;
        }
        return this.getName().equals((((com.mymovieapp.Movie) o).getName()));
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
     * gets average rating
     * @return Rating
     */
    public Rating getRating() {
        return rating;
    }
}
