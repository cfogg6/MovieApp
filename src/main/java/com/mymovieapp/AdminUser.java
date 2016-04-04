package com.mymovieapp;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

/**
 * User class made for specific use in Admin-related functions of application.
 */
public class AdminUser implements Comparable<AdminUser> {
    String name;
    boolean isLocked = false;
    boolean isBanned = false;
    Drawable statusImage = new Drawable() {
        @Override
        public void draw(Canvas canvas) {

        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }
    };
    Drawable profilePic = new Drawable() {
        @Override
        public void draw(Canvas canvas) {

        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }
    };

    /**
     * sets adminUser name to name passed in
     * @param name name passed in
     */
    public AdminUser(String name) {
        this.name = name;
    }

    /**
     * gets name of Admin
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * checks to see if AdminUser is locked
     * @return true if AdminUser is locked, or False if not
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * checks to see if AdminUser is banned
     * @return true if AdminUser is banned, or False if not
     */
    public boolean isBanned() {
        return isBanned;
    }

    /**
     * Sets name of AdminUser to parameter passed in
     * @param name new name that AdminUser will have
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets status of lock AdminUser
     * @param isLocked sets Admin's lock to isLocked
     */
    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    /**
     * sets status of Ban
     * @param isBanned sets Admin's ban to isBanned
     */
    public void setBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }

    /**
     * checks to see if two admin instances are the same or not
     * @param other the other admin that is being compared
     * @return true if both are equal, or false if not
     */
    public boolean equals(Object other) {
        return (other instanceof AdminUser) && ((AdminUser) other).name.equals(this.name);
    }

    @Override
    public int compareTo(AdminUser adminUser) {
        return this.name.compareToIgnoreCase(adminUser.name);
    }
}
