package com.mymovieapp;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Test for movie equals
 */
@SmallTest
@RunWith(AndroidJUnit4.class)
public class CoreyAndroidTest {

    @Test
    public void testMovieEquals() {
        Movie movie1 = new Movie("Deadpool", "1-1-1", "mypic", "Stuff happened", "1hr", "123", new Rating("Deadpool", "user"));
        Movie movie2 = new Movie("Deadpool2", "1-1-1", "mypic", "Stuff happened", "1hr", "123", new Rating("Deadpool", "user"));
        Movie movie3 = new Movie("Deadpool", "1-1-1", "mypic", "Stuff happened", "1hr", "123", new Rating("Deadpool", "user"));

        //Null check
        assertFalse(movie1.equals(null));

        //Not an instanceof
        assertFalse(movie1.equals("not a movie"));

        //Ratings aren't equal
        assertFalse(movie1.equals(movie2));

        //Ratings are equal
        assertTrue(movie1.equals(movie3));
    }
}
