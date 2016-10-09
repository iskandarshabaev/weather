package com.ishabaev.weather.cities;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ishabaev.weather.R;
import com.ishabaev.weather.addcity.AddCityActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ishabaev.weather.cities.utils.Actions.rotateScreen;
import static com.ishabaev.weather.cities.utils.Actions.waitId;
import static com.ishabaev.weather.cities.utils.Matchers.atPosition;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddCityScreenTest {

    @Rule
    public ActivityTestRule<AddCityActivity> mTasksActivityTestRule =
            new ActivityTestRule<AddCityActivity>(AddCityActivity.class) {

            };

    @Test
    public void typeCityNameAndCheckResult() {
        onView(withId(R.id.editText)).perform(typeText(String.valueOf("Moscow")));
        onView(isRoot()).perform(waitId(R.id.city_search_list, AddCityActivity.debounce));
        onView(withId(R.id.city_search_list))
                .check(matches(atPosition(0, hasDescendant(withText("Moscow")))));
    }

    @Test
    public void typeCityNameAndRotate() {
        onView(withId(R.id.editText)).perform(typeText(String.valueOf("Moscow")));
        for (int i = 0; i < 30; i++) {
            rotateScreen(mTasksActivityTestRule.getActivity());
        }
    }
}
