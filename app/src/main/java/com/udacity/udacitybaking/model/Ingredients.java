package com.udacity.udacitybaking.model;

import com.squareup.moshi.Json;

public class Ingredients {

  @Json(name = "quantity")
  private int quantity;

  @Json(name = "measure")
  private String measure;

  @Json(name = "ingredient")
  private String ingredient;

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getMeasure() {
    return measure;
  }

  public void setMeasure(String measure) {
    this.measure = measure;
  }

  public String getIngredient() {
    return ingredient;
  }

  public void setIngredient(String ingredient) {
    this.ingredient = ingredient;
  }

  @Override
  public String toString() {
    return "Ingredients{"
        + "quantity = '"
        + quantity
        + '\''
        + ",measure = '"
        + measure
        + '\''
        + ",ingredient = '"
        + ingredient
        + '\''
        + "}";
  }
}
