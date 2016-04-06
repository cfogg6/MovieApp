package com.mymovieapp;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Test for rating
 */
@SmallTest
@RunWith(AndroidJUnit4.class)
public class HoneyAndroidTest {

    @Test
    public void testRatingEquals() {
        Rating rating1 = new Rating("Deadpool", "Google");
        Rating rating2 = new Rating("Deadpool", "bob");
        Rating rating3 = new Rating("Deadpool 2", "HoneyHoney");

        Movie movie1 = new Movie("Deadpool", "2016", "123456", "", "", "", null);

        //Null check
        assertFalse(rating1.equals(null));

        //Not an instanceof
        assertFalse(rating1.equals(movie1));

        //Ratings aren't equal
        assertFalse(rating1.equals(rating3));

        //Ratings are equal
        assertTrue(rating1.equals(rating2));
    }
}
