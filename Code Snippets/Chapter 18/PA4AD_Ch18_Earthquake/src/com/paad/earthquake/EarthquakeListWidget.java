package com.paad.earthquake;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class EarthquakeListWidget extends AppWidgetProvider {
  
  @Override
  public void onUpdate(Context context,
                       AppWidgetManager appWidgetManager,
                       int[] appWidgetIds) {
    
    // Iterate over the array of active widgets.
    final int N = appWidgetIds.length;
    for (int i = 0; i < N; i++) {
      int appWidgetId = appWidgetIds[i];
      
      // Set up the intent that starts the Earthquake 
      // Remote Views Service, which will supply the views
      // shown in the List View.
      Intent intent = new Intent(context, EarthquakeRemoteViewsService.class);
      // Add the app widget ID to the intent extras.
      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

      // Instantiate the RemoteViews object for the App Widget layout.
      RemoteViews views = new RemoteViews(context.getPackageName(),
        R.layout.quake_collection_widget);
      
      // Set up the RemoteViews object to use a RemoteViews adapter. 
      views.setRemoteAdapter(R.id.widget_list_view, intent);
      
      // The empty view is displayed when the collection has no items. 
      views.setEmptyView(R.id.widget_list_view, R.id.widget_empty_text);
    
      // Create a Pending Intent template to provide interactivity to 
      // each item displayed within the collection View.
      Intent templateIntent = new Intent(context, Earthquake.class);
      templateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
      PendingIntent templatePendingIntent = 
        PendingIntent.getActivity(context, 0, templateIntent,
                                  PendingIntent.FLAG_UPDATE_CURRENT);
      
      views.setPendingIntentTemplate(R.id.widget_list_view, 
                                     templatePendingIntent);
      
      // Notify the App Widget Manager to update the widget using
      // the modified remote view.
      appWidgetManager.updateAppWidget(appWidgetId, views);
    }
  }
}
