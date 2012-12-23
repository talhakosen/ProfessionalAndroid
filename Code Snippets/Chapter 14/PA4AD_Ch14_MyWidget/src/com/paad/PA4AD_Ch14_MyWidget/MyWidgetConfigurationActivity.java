package com.paad.PA4AD_Ch14_MyWidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

public class MyWidgetConfigurationActivity extends Activity {
  /**
   * Listing 14-18: Skeleton App Widget configuration Activity 
   */
  private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      appWidgetId = extras.getInt(
        AppWidgetManager.EXTRA_APPWIDGET_ID, 
        AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    // Set the result to canceled in case the user exits
    // the Activity without accepting the configuration
    // changes / settings.
    setResult(RESULT_CANCELED, null);

    // Configure the UI.
  }

  private void completedConfiguration() {
    // Save the configuration settings for the Widget ID

    // Notify the Widget Manager that the configuration has completed.
    Intent result = new Intent();
    result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    setResult(RESULT_OK, result);
    finish();
  }
}