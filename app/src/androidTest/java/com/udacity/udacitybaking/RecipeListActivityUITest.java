package com.udacity.udacitybaking;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.udacitybaking.features.recipelist.RecipeListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeListActivityUITest {

  @Rule
  public final ActivityTestRule<RecipeListActivity> activityTestRule =
      new ActivityTestRule<>(RecipeListActivity.class);

  private IdlingResource idlingResource;

  @Before
  public void registerIdlingResource() {
    idlingResource = activityTestRule.getActivity().getIdlingResource();
    IdlingRegistry.getInstance().register(idlingResource);
  }

  @Test
  public void clickOnRecyclerViewItem_opensRecipeDetailsActivity() {
    onView(withId(R.id.rv_recipe_list))
        .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

    onView(withId(R.id.tv_ingredients_content)).check(matches(isDisplayed()));
  }

  @After
  public void unregisterIdlingResource() {
    if (idlingResource != null) {
      IdlingRegistry.getInstance().unregister(idlingResource);
    }
  }
}
