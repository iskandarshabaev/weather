package com.ishabaev.weather.cities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import com.ishabaev.weather.R;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringEndsWith.endsWith;

/**
 * Created by ishabaev on 28.07.16.
 */


@RunWith(AndroidJUnit4.class)
@LargeTest
public class CitiesScreenTest {

    @Rule
    public ActivityTestRule<CitiesActivity> mTasksActivityTestRule =
            new ActivityTestRule<CitiesActivity>(CitiesActivity.class) {
                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();
                }
            };


    @Test
    public void addCities() {
        for (char alphabet = 'a'; alphabet <= 'c'; alphabet++) {
            for (int i = 0; i < 2; i++) {
                onView(withId(R.id.fab)).perform(click());
                onView(withId(R.id.editText)).perform(typeText(String.valueOf(alphabet)));
                onView(isRoot()).perform(waitId(R.id.city_search_list, 2000));
                onView(withId(R.id.city_search_list))
                        .perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
            }
        }
    }


    @Test
    public void addCity() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.editText)).perform(typeText(String.valueOf("Ka")));
        onView(isRoot()).perform(waitId(R.id.city_search_list, 6000));
        onView(withId(R.id.city_search_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }



    public static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {

                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;

                while (System.currentTimeMillis() < endTime) ;
            }
        };
    }


    @Test
    public void swipteToDelete() {
        while (true) {
            try {
                onView(withId(R.id.city_list))
                        .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeRight()));
            } catch (Exception e) {
                break;
            }
        }
        onView(withId(R.id.swipe_to_refresh)).perform(swipeDown());
    }

    public static ViewAction swipeRight() {
        return new GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER_LEFT,
                GeneralLocation.CENTER_RIGHT, Press.FINGER);
    }

    @Test
    public void swipeToRefresh() {
        for (int i = 0; i < 10; i++) {
            rotateScreen();
            onView(withId(R.id.swipe_to_refresh))
                    .perform(swipeDown());
        }
    }

    private void rotateScreen() {
        Context context = InstrumentationRegistry.getTargetContext();
        int orientation
                = context.getResources().getConfiguration().orientation;

        Activity activity = mTasksActivityTestRule.getActivity();
        activity.setRequestedOrientation(
                (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Test
    public void clickCityCard() {
        onView(withId(R.id.city_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
    }


    private static int getResourceId(String s) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        String packageName = targetContext.getPackageName();
        return targetContext.getResources().getIdentifier(s, "id", packageName);
    }

    @Test
    public void clickAddCity() {
        // Click on the add task button
        onView(withId(R.id.fab)).perform(click());

        // Check if the add task screen is displayed
        //onView(withId(R.id.add_task_title)).check(matches(isDisplayed()));
    }

    public static ViewAction withCustomConstraints(final ViewAction action, final Matcher<View> constraints) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return constraints;
            }

            @Override
            public String getDescription() {
                return action.getDescription();
            }

            @Override
            public void perform(UiController uiController, View view) {
                action.perform(uiController, view);
            }
        };
    }


}
