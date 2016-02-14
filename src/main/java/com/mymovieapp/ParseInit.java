package com.mymovieapp;

import android.app.Application;

import com.parse.Parse;

/**
 * Initializes the Parse Database Connection for the Application.
 */
public class ParseInit extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
    }
}
