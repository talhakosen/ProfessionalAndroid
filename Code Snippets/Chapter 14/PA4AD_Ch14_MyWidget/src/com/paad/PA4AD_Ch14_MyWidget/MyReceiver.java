package com.paad.PA4AD_Ch14_MyWidget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    /**
     * Listing 14-9: Accessing the App Widget Manager
     */
    // Get the App Widget Manager.
    AppWidgetManager appWidgetManager 
      = AppWidgetManager.getInstance(context);

    // Retrieve the identifiers for each instance of your chosen widget.
    ComponentName thisWidget = new ComponentName(context, MyAppWidget.class);
    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

    /**
     * Listing 14-10: A standard pattern for updating Widget UI
     */
    final int N = appWidgetIds.length;
    // Iterate through each widget, creating a RemoteViews object and
    // applying the modified RemoteViews to each widget.
    for (int i = 0; i < N; i++) {
      int appWidgetId = appWidgetIds[i];
      // Create a Remote View
      RemoteViews views = new RemoteViews(context.getPackageName(),
                                          R.layout.my_widget_layout);

      // TODO Update the widget UI using the views object.

      // Notify the App Widget Manager to update the widget using
      // the modified remote view.
      appWidgetManager.updateAppWidget(appWidgetId, views);
    }
  }

}
