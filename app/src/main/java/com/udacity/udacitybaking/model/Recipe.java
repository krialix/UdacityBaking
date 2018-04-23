package com.udacity.udacitybaking.model;

import com.squareup.moshi.Json;

import java.util.List;

public class Recipe {

  @Json(name = "image")
  private String image;

  @Json(name = "servings")
  private int servings;

  @Json(name = "name")
  private String name;

  @Json(name = "ingredients")
  private List<Ingredients> ingredients;

  @Json(name = "id")
  private int id;

  @Json(name = "steps")
  private List<Steps> steps;

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public int getServings() {
    return servings;
  }

  public void setServings(int servings) {
    this.servings = servings;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Ingredients> getIngredients() {
    return ingredients;
  }

  public void setIngredients(List<Ingredients> ingredients) {
    this.ingredients = ingredients;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<Steps> getSteps() {
    return steps;
  }

  public void setSteps(List<Steps> steps) {
    this.steps = steps;
  }

  @Override
  public String toString() {
    return "Recipe{"
        + "image = '"
        + image
        + '\''
        + ",servings = '"
        + servings
        + '\''
        + ",name = '"
        + name
        + '\''
        + ",ingredients = '"
        + ingredients
        + '\''
        + ",id = '"
        + id
        + '\''
        + ",steps = '"
        + steps
        + '\''
        + "}";
  }
}
