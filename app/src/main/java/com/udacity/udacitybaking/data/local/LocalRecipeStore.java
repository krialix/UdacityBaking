package com.udacity.udacitybaking.data.local;

import com.udacity.udacitybaking.data.model.Recipe;

public class LocalRecipeStore {

  private Recipe lastAccessedRecipe;

  private LocalRecipeStore() {}

  public static LocalRecipeStore create() {
    return new LocalRecipeStore();
  }

  public Recipe getLastAccessedRecipe() {
    return lastAccessedRecipe;
  }

  public void setLastAccessedRecipe(Recipe recipe) {
    this.lastAccessedRecipe = recipe;
  }
}
