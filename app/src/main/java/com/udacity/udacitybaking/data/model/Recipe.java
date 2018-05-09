package com.udacity.udacitybaking.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.util.List;
import java.util.Objects;

public class Recipe implements Parcelable {

  public static final Parcelable.Creator<Recipe> CREATOR =
      new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
          return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
          return new Recipe[size];
        }
      };

  @Json(name = "image")
  private String image;
  @Json(name = "servings")
  private int servings;
  @Json(name = "name")
  private String name;
  @Json(name = "ingredients")
  private List<Ingredient> ingredients;
  @Json(name = "id")
  private int id;
  @Json(name = "steps")
  private List<Step> steps;

  public Recipe() {}

  protected Recipe(Parcel in) {
    this.image = in.readString();
    this.servings = in.readInt();
    this.name = in.readString();
    this.ingredients = in.createTypedArrayList(Ingredient.CREATOR);
    this.id = in.readInt();
    this.steps = in.createTypedArrayList(Step.CREATOR);
  }

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

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public void setIngredients(List<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<Step> getSteps() {
    return steps;
  }

  public void setSteps(List<Step> steps) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Recipe recipe = (Recipe) o;
    return servings == recipe.servings
        && id == recipe.id
        && Objects.equals(image, recipe.image)
        && Objects.equals(name, recipe.name)
        && Objects.equals(ingredients, recipe.ingredients)
        && Objects.equals(steps, recipe.steps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(image, servings, name, ingredients, id, steps);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.image);
    dest.writeInt(this.servings);
    dest.writeString(this.name);
    dest.writeTypedList(this.ingredients);
    dest.writeInt(this.id);
    dest.writeTypedList(this.steps);
  }
}
