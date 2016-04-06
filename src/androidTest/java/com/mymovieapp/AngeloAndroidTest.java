package com.mymovieapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.ImageView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

/**
 * Test for login class
 */
@SmallTest
@RunWith(AndroidJUnit4.class)
public class AngeloAndroidTest {

    String testN = "Test Name";
    String testD = "January 1, 1990";
    String testP = "http://ia.media-imdb.com/images/M/MV5BMjQyODg5Njc4N15BMl5BanBnXkFtZTgwMzExMjE3NzE@._V1_SX300.jpg";
    String testS = "This was a movie. It was great.";
    String testR = "103 min";
    String testI = "1030183";
    Rating testRa = new Rating("Deadpool", "Google");

    @Rule
    public ActivityTestRule<MovieInfoActivity> mActivityRule = new ActivityTestRule<>(
            MovieInfoActivity.class, true, false);

    @Test
    public void checkMovieInfo() {
        Movie mov = new Movie(testN, testD, testP, testS, testR, testI, testRa);
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, MovieInfoActivity.class);
        intent.putExtra("SALTY_POPCORN_CURRENT_MOVIE", mov);
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.tv_movie_title)).check(matches(withText(testN)));
        onView(withId(R.id.tV_synopsis)).check(matches(withText(testS)));
        try {
            Bitmap testPic = BitmapFactory.decodeStream(new java.net.URL(testP).openStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            testPic.compress(Bitmap.CompressFormat.PNG, 100, bos);
            final byte[] testBitMapVal = bos.toByteArray();

            ImageView imageView = (ImageView) mActivityRule.getActivity().findViewById(R.id.iV_movPhoto);

            Bitmap viewPic = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
            viewPic.compress(Bitmap.CompressFormat.PNG, 100, bos2);
            final byte[] viewBitMapVal = bos2.toByteArray();

            assertTrue(java.util.Arrays.equals(testBitMapVal, viewBitMapVal));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkNoSynopsis() {
        Movie mov = new Movie(testN, testD, testP, "", testR, testI, testRa);
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, MovieInfoActivity.class);
        intent.putExtra("SALTY_POPCORN_CURRENT_MOVIE", mov);
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.tv_movie_title)).check(matches(withText(testN)));
        onView(withId(R.id.tV_synopsis)).check(matches(withText("No synopsis.")));
        try {
            Bitmap testPic = BitmapFactory.decodeStream(new java.net.URL(testP).openStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            testPic.compress(Bitmap.CompressFormat.PNG, 100, bos);
            final byte[] testBitMapVal = bos.toByteArray();

            ImageView imageView = (ImageView) mActivityRule.getActivity().findViewById(R.id.iV_movPhoto);

            Bitmap viewPic = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
            viewPic.compress(Bitmap.CompressFormat.PNG, 100, bos2);
            final byte[] viewBitMapVal = bos2.toByteArray();

            assertTrue(java.util.Arrays.equals(testBitMapVal, viewBitMapVal));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
