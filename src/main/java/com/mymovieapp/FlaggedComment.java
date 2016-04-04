package com.mymovieapp;

/**
 * Creats flagged comment
 * Created by Honey on 3/28/2016.
 */
public class FlaggedComment {

    String title;
    String comment;
    String username;
    boolean isDeleted = false;

    /**
     * Details for flagged comments
     * @param title title of movie
     * @param comment comment that was flagged
     * @param username username of user that made comment
     */
    public FlaggedComment(String title, String comment, String username) {
        this.title = title;
        this.comment = comment;
        this.username = username;
    }

    /**
     * gets title of movie for specific flagged comment
     * @return String title of movie for flagged comment
     */
    public String getTitle() {
        return title;
    }

    /**
     * gets comment that was flagged
     * @return String comment that was flagged
     */
    public String getComment() {
        return comment;
    }

    /**
     * gets username of user who posted the comment
     * @return  String username
     */
    public String getUsername() {
        return username;
    }

    /**
     * checks if comment is deleted
     * @return true if deleted, false if not
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * set deleted to isDeleted
     * @param isDeleted true if comment is deleted, false if not
     */
    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
