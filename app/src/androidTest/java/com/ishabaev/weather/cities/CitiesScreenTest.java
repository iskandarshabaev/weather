package com.ishabaev.weather.cities;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ishabaev.weather.R;
import com.ishabaev.weather.cities.utils.Actions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.ishabaev.weather.cities.utils.Actions.swipeRight;
import static com.ishabaev.weather.cities.utils.Actions.waitId;

/**
 * Created by ishabaev on 28.07.16.
 */


@RunWith(AndroidJUnit4.class)
@LargeTest
public class CitiesScreenTest {

    @Rule
    public ActivityTestRule<CitiesActivity> mTasksActivityTestRule =
            new ActivityTestRule<CitiesActivity>(CitiesActivity.class) {
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

    @Test
    public void swipeToDelete() {
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

    @Test
    public void swipeToRefresh() {
        for (int i = 0; i < 10; i++) {
            Actions.rotateScreen(mTasksActivityTestRule.getActivity());
            onView(withId(R.id.swipe_to_refresh))
                    .perform(swipeDown());
        }
    }

    @Test
    public void loadAllCities() {
        onView(withId(R.id.swipe_to_refresh)).perform(swipeDown());
        onView(withId(R.id.city_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

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
}
