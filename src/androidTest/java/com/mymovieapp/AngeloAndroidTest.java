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

/**
 * Test for login class
 */
@SmallTest
@RunWith(AndroidJUnit4.class)
public class AngeloAndroidTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Test
    public void testChangeText_login() {
        onView(withId(R.id.et_username))
                .perform(typeText("Google"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.btn_login))
                .perform(click());
    }
}
