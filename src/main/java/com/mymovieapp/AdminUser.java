package com.mymovieapp;

import java.util.Objects;

/**
 * Created by Corey on 3/13/16.
 */
public class AdminUser {
    String name;
    boolean isLocked = false;
    boolean isBanned = false;

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
        if (other instanceof AdminUser) {
            return ((AdminUser) other).name.equals(this.name);
        } else {
            return false;
        }
    }

    public int compareTo(AdminUser adminUser) {
        return adminUser.name.compareTo(this.name);
    }
}
