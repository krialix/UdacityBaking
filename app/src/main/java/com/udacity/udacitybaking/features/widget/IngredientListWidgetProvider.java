package com.udacity.udacitybaking.features.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udacity.udacitybaking.BakingApp;
import com.udacity.udacitybaking.R;
import com.udacity.udacitybaking.data.local.LocalRecipeStore;
import com.udacity.udacitybaking.data.model.Ingredient;
import com.udacity.udacitybaking.data.model.Recipe;
import com.udacity.udacitybaking.features.recipedetail.RecipeDetailActivity;
import com.udacity.udacitybaking.util.IngredientUtil;

public class IngredientListWidgetProvider extends AppWidgetProvider {

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
    LocalRecipeStore localRecipeStore = BakingApp.LOCAL_RECIPE_STORE;
    Recipe lastAccessedRecipe = localRecipeStore.getLastAccessedRecipe();

    if (lastAccessedRecipe == null) {
      return;
    }

    Intent intent = new Intent(context, RecipeDetailActivity.class);
    intent.putExtra(RecipeDetailActivity.KEY_RECIPE, lastAccessedRecipe);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients_list);

    views.setOnClickPendingIntent(R.id.widget_ingredients_container, pendingIntent);
    views.setTextViewText(R.id.widget_recipe_name, lastAccessedRecipe.getName());

    views.removeAllViews(R.id.widget_ingredients_container);

    for (Ingredient ingredient : lastAccessedRecipe.getIngredients()) {
      RemoteViews ingredientView =
          new RemoteViews(context.getPackageName(), R.layout.item_widget_ingredient);

      String line =
          IngredientUtil.formatIngredient(
              context,
              ingredient.getIngredient(),
              ingredient.getQuantity(),
              ingredient.getMeasure());

      ingredientView.setTextViewText(R.id.widget_ingredient_name, line);
      views.addView(R.id.widget_ingredients_container, ingredientView);
    }

    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}
