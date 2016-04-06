package com.mymovieapp;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;


/**
 * Test for rating
 */
@SmallTest
@RunWith(AndroidJUnit4.class)
public class ApurvaAndroidTest {

    @Test
    public void testRatingCompareTo() {
        Rating rating1 = new Rating("Deadpool", "Google");
        rating1.addRating(3.0);
        Rating rating2 = new Rating("Deadpool", "bob");
        rating2.addRating(4.0);
        Rating rating3 = new Rating("Deadpool 2", "Angie");
        rating3.addRating(5.0);
        Rating rating4 = new Rating("Deadpool", "Emily");
        rating4.addRating(4.0);

        //first one is greater than second
        assertEquals(-1, rating2.compareTo(rating1));

        //second one is greater
        assertEquals(1, rating2.compareTo(rating3));

        //Ratings are equal
        assertEquals(0, rating2.compareTo(rating4));
    }
}
