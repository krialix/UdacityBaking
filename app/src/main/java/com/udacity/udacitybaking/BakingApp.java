package com.udacity.udacitybaking;

import android.app.Application;

import com.udacity.udacitybaking.data.local.LocalRecipeStore;

public class BakingApp extends Application {

  public static final LocalRecipeStore LOCAL_RECIPE_STORE = LocalRecipeStore.create();
}
