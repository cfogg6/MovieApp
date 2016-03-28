package com.mymovieapp;

/**
 * Created by Honey on 3/28/2016.
 */
public class FlaggedComment {

    String title;
    String comment;
    String username;
    boolean isDeleted = false;

    public FlaggedComment(String title, String comment, String username) {
        this.title = title;
        this.comment = comment;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
