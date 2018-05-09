package com.udacity.udacitybaking.features.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class UpdateWidgetIntent extends IntentService {

  private static final String KEY_INGREDIENTS = "INGREDIENTS";

  private static final String TAG = "UpdateWidgetIntent";

  public UpdateWidgetIntent() {
    super(TAG);
  }

  public static void start(Context context, ArrayList<String> ingredients) {
    Intent starter = new Intent(context, UpdateWidgetIntent.class);
    starter.putExtra(KEY_INGREDIENTS, ingredients);
    context.startActivity(starter);
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    if (intent != null) {
      ArrayList<String> ingredients = intent.getExtras().getStringArrayList(KEY_INGREDIENTS);
      handleUpdateWidgets(ingredients);
    }
  }

  private void handleUpdateWidgets(ArrayList<String> ingredients) {
    Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
    intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
    intent.putExtra(KEY_INGREDIENTS, ingredients);
    sendBroadcast(intent);
  }
}
