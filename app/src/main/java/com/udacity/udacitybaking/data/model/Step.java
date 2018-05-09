package com.udacity.udacitybaking.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.util.Objects;

public class Step implements Parcelable {

  public static final Parcelable.Creator<Step> CREATOR =
      new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
          return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
          return new Step[size];
        }
      };

  @Json(name = "videoURL")
  private String videoURL;

  @Json(name = "description")
  private String description;

  @Json(name = "id")
  private int id;

  @Json(name = "shortDescription")
  private String shortDescription;

  @Json(name = "thumbnailURL")
  private String thumbnailURL;

  public Step() {}

  protected Step(Parcel in) {
    this.videoURL = in.readString();
    this.description = in.readString();
    this.id = in.readInt();
    this.shortDescription = in.readString();
    this.thumbnailURL = in.readString();
  }

  public String getVideoURL() {
    return videoURL;
  }

  public void setVideoURL(String videoURL) {
    this.videoURL = videoURL;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getThumbnailURL() {
    return thumbnailURL;
  }

  public void setThumbnailURL(String thumbnailURL) {
    this.thumbnailURL = thumbnailURL;
  }

  @Override
  public String toString() {
    return "Step{"
        + "videoURL = '"
        + videoURL
        + '\''
        + ",description = '"
        + description
        + '\''
        + ",id = '"
        + id
        + '\''
        + ",shortDescription = '"
        + shortDescription
        + '\''
        + ",thumbnailURL = '"
        + thumbnailURL
        + '\''
        + "}";
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.videoURL);
    dest.writeString(this.description);
    dest.writeInt(this.id);
    dest.writeString(this.shortDescription);
    dest.writeString(this.thumbnailURL);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Step step = (Step) o;
    return id == step.id
        && Objects.equals(videoURL, step.videoURL)
        && Objects.equals(description, step.description)
        && Objects.equals(shortDescription, step.shortDescription)
        && Objects.equals(thumbnailURL, step.thumbnailURL);
  }

  @Override
  public int hashCode() {
    return Objects.hash(videoURL, description, id, shortDescription, thumbnailURL);
  }
}
