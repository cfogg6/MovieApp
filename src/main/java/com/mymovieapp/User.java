package com.mymovieapp;

/**
 * Created by Honey on 2/12/2016.
 */
public class User {

    private String username;
    private String name;
    private String password;
    private String email;
    private boolean isBanned;
    private String major;
    private String interests;

    /**
     * Creates a new user who provided their interests
     * @param username new user's username
     * @param name new user's name
     * @param password new user's password
     * @param email new user's email
     * @param major new user's major
     * @param interests new user's interests
     */
    public User(String username, String name, String password, String email, String major, String interests) {
        this(username, name, password, email, major);
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
    public User(String username, String name, String password, String email, String major) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.major = major;
        isBanned = false;
    }

    /**
     * Returns a user's login username
     * @return user's login username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns a user's name
     * @return user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a user's email
     * @return user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns a user's major
     * @return user's major
     */
    public String getMajor() {
        return major;
    }

    /**
     * Returns a user's interests
     * @return user's interests
     */
    public String getInterests() {
        return interests;
    }
}
