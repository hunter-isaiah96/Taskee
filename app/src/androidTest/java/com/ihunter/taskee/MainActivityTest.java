package com.ihunter.taskee;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ihunter.taskee.activities.MainActivity;

import org.junit.Rule;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Master Bison on 12/23/2016.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @org.junit.Test
    public void shouldBeAbleToLaunchMainScreen(){
        onView(withText("Hello")).check(ViewAssertions.matches(isDisplayed()));
    }

}
