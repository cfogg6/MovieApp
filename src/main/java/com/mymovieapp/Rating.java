package com.mymovieapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Corey on 3/8/16.
 */
public class Rating implements Parcelable, Comparable {

    String name;
    String username;
    ArrayList<Double> list = new ArrayList<>();

    /**
     * Create Rating
     * @param name Movie name
     * @param username User who is looking at the rating
     */
    public Rating(String name, String username) {
        this.name = name;
        this.username = username;
    }

    @Override
    public int compareTo(Object rating) {
        if (this.getAverageRating() < ((Rating) rating).getAverageRating()) {
            return 1;
        } else if (this.getAverageRating() > ((Rating) rating).getAverageRating()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Rating) && name.equals(((Rating)other).name);
    }

    /**
     * Add a rating to the list of ratings
     * @param rating Rating to be added
     */
    public void addRating(Double rating) {
        list.add(rating);
    }

    /**
     * Get the average rating for this movie
     * @return the average rating
     */
    public double getAverageRating() {
        DecimalFormat format = new DecimalFormat("#.#");
        double sum = 0;
        for (Double item: list) {
            sum += item;
        }
        return Double.valueOf(format.format(sum / list.size()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * makes rating a parcel
     * @param out parcel being moved out
     * @param flags flags
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(username);
        out.writeList(list);
    }

    public static final Parcelable.Creator<Rating> CREATOR
            = new Parcelable.Creator<Rating>() {
        public Rating createFromParcel(Parcel in) {
            return new Rating(in);
        }

        public Rating[] newArray(int size) {
            return new Rating[size];
        }
    };

    /**
     * Create rating from a parcel
     * @param in Parcel to read
     */
    private Rating(Parcel in) {
        name = in.readString();
        username = in.readString();
        in.readList(list, Double.class.getClassLoader());
    }
}
