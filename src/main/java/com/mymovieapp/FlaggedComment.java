package com.mymovieapp;

/**
 * Create flagged comment
 * Created by Honey on 3/28/2016.
 */
public class FlaggedComment {

    /**
     * Title of the movie
     */
    private final String title;
    /**
     * Comment
     */
    private final String comment;
    /**
     * Username of the user who gave the comment
     */
    private final String username;
    /**
     * Whether or not the comment is deleted or not
     */
    private boolean commentIsDeleted = false;

    /**
     * Details for flagged comments
     * @param t title of movie
     * @param c comment that was flagged
     * @param u username of user that made comment
     */
    public FlaggedComment(String t, String c, String u) {
        this.title = t;
        this.comment = c;
        this.username = u;
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
        return commentIsDeleted;
    }

    /**
     * set deleted to isDeleted
     * @param b true if comment is deleted, false if not
     */
    public void setDeleted(boolean b) {
        this.commentIsDeleted = b;
    }
}
