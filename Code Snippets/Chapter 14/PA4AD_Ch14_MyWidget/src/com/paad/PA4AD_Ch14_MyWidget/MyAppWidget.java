package com.paad.PA4AD_Ch14_MyWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyAppWidget extends AppWidgetProvider {
  
  /**
   * Listing 14-8: Using a Remote View within the App Widget Provider's onUpdate Handler
   */
  @Override
  public void onUpdate(Context context,
                       AppWidgetManager appWidgetManager,
                       int[] appWidgetIds) {
    // Iterate through each widget, creating a RemoteViews object and
    // applying the modified RemoteViews to each widget.
    final int N = appWidgetIds.length;
    for (int i = 0; i < N; i++) {
      int appWidgetId = appWidgetIds[i];

      // Create a Remote View
      /**
       * Listing 14-5: Creating Remote Views
       */
      RemoteViews views = new RemoteViews(context.getPackageName(),
                                          R.layout.my_widget_layout);

      // TODO Update the UI
      /**
       * Listing 14-11: Adding a Click Listener to an App Widget
       */
      Intent intent = new Intent(context, MyActivity.class);
      PendingIntent pendingIntent = 
        PendingIntent.getActivity(context, 0, intent, 0);
      views.setOnClickPendingIntent(R.id.widget_text, pendingIntent);

      // Notify the App Widget Manager to update the widget using
      // the modified remote view.
      appWidgetManager.updateAppWidget(appWidgetId, views);
    }
  }

  /**
   * Listing 14-14: Updating App Widgets based on Broadcast Intents
   */
  public static String FORCE_WIDGET_UPDATE =
    "com.paad.mywidget.FORCE_WIDGET_UPDATE";

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);

    if (FORCE_WIDGET_UPDATE.equals(intent.getAction())) {
      // TODO Update widget
    }
  }

}