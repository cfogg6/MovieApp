package com.mymovieapp;

/**
 * Created by Corey on 3/8/16.
 */
public class Rating implements Comparable {

    String name;
    String username;
    double rating;

    public Rating(String name, String username, double rating) {
        this.name = name;
        this.username = username;
        this.rating = rating;
    }

    @Override
    public int compareTo(Object rating) {
        return 0;
    }
}
