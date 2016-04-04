package com.mymovieapp;

/**
 * User class made for specific use in Admin-related functions of application.
 */
public class AdminUser implements Comparable<AdminUser> {
    /**
     * Name of user
     */
    private String name;
    /**
     * Is the user account locked
     */
    private boolean userIsLocked = false;
    /**
     * Is the user account banned
     */
    private boolean userIsBanned = false;
//    private Drawable statusImage = new Drawable() {
//        @Override
//        public void draw(Canvas canvas) {
//        }
//
//        @Override
//        public void setAlpha(int alpha) {
//        }
//
//        @Override
//        public void setColorFilter(ColorFilter colorFilter) {
//        }
//
//        @Override
//        public int getOpacity() {
//            return 0;
//        }
//    };
//    Drawable profilePic = new Drawable() {
//        @Override
//        public void draw(Canvas canvas) {
//        }
//
//        @Override
//        public void setAlpha(int alpha) {
//        }
//
//        @Override
//        public void setColorFilter(ColorFilter colorFilter) {
//        }
//
//        @Override
//        public int getOpacity() {
//            return 0;
//        }
//    };

    /**
     * sets adminUser name to name passed in
     * @param n name passed in
     */
    public AdminUser(String n) {
        this.name = n;
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
        return userIsLocked;
    }

    /**
     * checks to see if AdminUser is banned
     * @return true if AdminUser is banned, or False if not
     */
    public boolean isBanned() {
        return userIsBanned;
    }

    /**
     * Sets name of AdminUser to parameter passed in
     * @param n new name that AdminUser will have
     */
    public void setName(String n) {
        this.name = n;
    }

    /**
     * sets status of lock AdminUser
     * @param isLocked sets Admin's lock to userIsLocked
     */
    public void setUserIsLocked(boolean isLocked) {
        this.userIsLocked = isLocked;
    }

    /**
     * sets status of Ban
     * @param isBanned sets Admin's ban to isBanned
     */
    public void setBanned(boolean isBanned) {
        this.userIsBanned = isBanned;
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
