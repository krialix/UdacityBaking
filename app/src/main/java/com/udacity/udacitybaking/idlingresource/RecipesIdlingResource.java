package com.udacity.udacitybaking.idlingresource;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class RecipesIdlingResource implements IdlingResource {

  private final AtomicBoolean isIdleNow;

  private volatile ResourceCallback callback;

  public RecipesIdlingResource() {
    isIdleNow = new AtomicBoolean(true);
  }

  @Override
  public String getName() {
    return this.getClass().getName();
  }

  @Override
  public boolean isIdleNow() {
    return isIdleNow.get();
  }

  @Override
  public void registerIdleTransitionCallback(ResourceCallback callback) {
    this.callback = callback;
  }

  public void setIdleState(boolean idleState) {
    isIdleNow.set(idleState);
    if (callback != null && idleState) {
      callback.onTransitionToIdle();
    }
  }
}
