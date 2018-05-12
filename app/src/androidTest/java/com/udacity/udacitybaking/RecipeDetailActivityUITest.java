package com.udacity.udacitybaking;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.udacitybaking.data.model.Ingredient;
import com.udacity.udacitybaking.data.model.Recipe;
import com.udacity.udacitybaking.features.recipedetail.RecipeDetailActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeDetailActivityUITest {

  @Rule
  public final ActivityTestRule<RecipeDetailActivity> activityTestRule =
      new ActivityTestRule<>(RecipeDetailActivity.class, true, false);

  @Before
  public void setup() {
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(buildIngredient());

    Recipe recipe = buildRecipe(ingredients);

    Intent intent = new Intent();
    intent.putExtra(RecipeDetailActivity.KEY_RECIPE, recipe);

    activityTestRule.launchActivity(intent);
  }

  @Test
  public void checkUiElements() {
    onView(withId(R.id.tv_ingredients_title)).check(matches(isDisplayed()));
    onView(withId(R.id.tv_ingredients_title))
        .check(matches(withText(R.string.overview_ingredients_title)));

    onView(withId(R.id.tv_ingredients_content)).check(matches(isDisplayed()));
  }

  private Ingredient buildIngredient() {
    Ingredient ingredient = new Ingredient();
    ingredient.setIngredient("ingredient 1");
    ingredient.setQuantity(10);
    ingredient.setMeasure("g");
    return ingredient;
  }

  private Recipe buildRecipe(List<Ingredient> ingredients) {
    Recipe recipe = new Recipe();
    recipe.setId(1);
    recipe.setName("Recipe 1");
    recipe.setServings(5);
    recipe.setIngredients(ingredients);
    recipe.setSteps(Collections.emptyList());
    return recipe;
  }
}
