package com.mymovieapp;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Corey on 3/8/16.
 */
public class Rating implements Comparable {

    String name;
    String username;
    ArrayList<Double> list = new ArrayList<>();

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
        if (!(other instanceof Rating)) {
            return false;
        }
        return name.equals(((Rating)other).name);
    }

    public void addRating(Double rating) {
        list.add(rating);
    }

    public double getAverageRating() {
        DecimalFormat format = new DecimalFormat("#.#");
        double sum = 0;
        for (Double item: list)
            sum += item;
        return Double.valueOf(format.format(sum / list.size()));
    }
}
