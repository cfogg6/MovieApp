package com.mymovieapp;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

/**
 * User class made for specific use in Admin-related functions of application.
 */
public class AdminUser {
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

    public AdminUser(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public void setBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }

    public boolean equals(Object other) {
        return (other instanceof AdminUser) && ((AdminUser) other).name.equals(this.name);
    }

    public int compareTo(AdminUser adminUser) {
        return adminUser.name.compareTo(this.name);
    }
}
