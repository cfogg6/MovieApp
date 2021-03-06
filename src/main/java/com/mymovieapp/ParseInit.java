package com.mymovieapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseUser;

/**
 * Initializes the Parse Database Connection for the Application.
 */
public class ParseInit extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Connect to the Parse backend
        Parse.initialize(this);

        ParseUser.enableAutomaticUser();
    }
}
