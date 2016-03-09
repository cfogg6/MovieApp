package com.mymovieapp;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import javax.security.auth.callback.Callback;

public class User {

    private String username;
    private String name;
    private String password;
    private String email;
    private boolean isBanned;
    private String major;
    private String interests;
    private ParseUser parseUser;

    /**
     * Creates a new user who provided their interests
     * @param username new user's username
     * @param name new user's name
     * @param password new user's password
     * @param email new user's email
     * @param major new user's major
     * @param interests new user's interests
     */
    public User(String username, String name, String password, String email, String major,
                String interests, ParseUser parseUser) {
        this(username, name, password, email, major, parseUser);
        this.interests = interests;
    }

    /**
     * Creates a new user who did not provide their interests
     * @param username new user's username
     * @param name new user's name
     * @param password new user's password
     * @param email new user's email
     * @param major new user's major
     */
    public User(String username, String name, String password, String email, String major,
                ParseUser parseUser) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.major = major;
        isBanned = false;
        this.parseUser = parseUser;
    }

    /**
     * Returns a user's login username
     * @return user's login username
     */
    public String getUsername() {
        return parseUser.getUsername();
    }

    /**
     * Returns a user's name
     * @return user's name
     */
    public String getName() {
        return parseUser.getString("name");
    }

    /**
     * Returns a user's email
     * @return user's email
     */
    /*public static String getEmail() {
        return ParseUser.getCurrentUser().getEmail();
    }*/

    /**
     * Returns a user's major
     * @return user's major
     */
    public String getMajor() {
        return parseUser.getString("major");
    }

    /**
     * Returns a user's interests
     * @return user's interests
     */
    public String getInterests() {
        return parseUser.getString("interests");
    }

    /**
     * Returns a user's interests
     * @return user's interests
     */
    public void login(String username, String password, LogInCallback logInCallback) {
        parseUser.logInInBackground(username, password, logInCallback);
    }
}
