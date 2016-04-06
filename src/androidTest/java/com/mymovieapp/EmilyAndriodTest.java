package com.mymovieapp;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
//import static junit.framework.Assert.assertFalse;
//import static junit.framework.Assert.assertTrue;

/**
 * Test for rating
 */
@SmallTest
@RunWith(AndroidJUnit4.class)
public class EmilyAndriodTest {

    @Test
    public void testRatingAvgs() {
        Rating rating1 = new Rating("Deadpool", "Google");
        rating1.addRating(3.0);
        double avgR1 = rating1.getAverageRating();
        assertEquals(avgR1, 3.0);

        rating1.addRating(5.0);
        avgR1 = rating1.getAverageRating();
        assertEquals(avgR1, 4.0);

        Rating rating2 = new Rating("Deadpool 2", "HoneyHoney");
        rating2.addRating(5.0);
        avgR1 = rating1.getAverageRating();
        double avgR2 = rating2.getAverageRating();
        assertEquals(avgR1, 4.0);
        assertEquals(avgR2, 5.0);

        rating2.addRating(1.0);
        avgR2 = rating2.getAverageRating();
        assertEquals(avgR2, 3.0);

        rating2.addRating(2.0);
        avgR2 = rating2.getAverageRating();
        assertEquals(avgR2, 2.7);


    }
}
