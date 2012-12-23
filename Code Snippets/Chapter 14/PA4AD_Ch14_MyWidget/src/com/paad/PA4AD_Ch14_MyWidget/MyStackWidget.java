package com.paad.PA4AD_Ch14_MyWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyStackWidget extends AppWidgetProvider {

  /**
   * Listing 14-24: Binding a Remove Views Service to a Widget
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

      // Create a Remote View.
      RemoteViews views = new RemoteViews(context.getPackageName(),
        R.layout.my_stack_widget_layout);
      
      // Bind this widget to a Remote Views Service.
      Intent intent = new Intent(context, MyRemoteViewsService.class);
      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
      views.setRemoteAdapter(appWidgetId, R.id.widget_stack_view,
                             intent);
    
      // Specify a View within the Widget layout hierarchy to display 
      // when the bound collection is empty.
      views.setEmptyView(R.id.widget_stack_view, R.id.widget_empty_text);

      // TODO Customize this Widgets UI based on configuration 
      // settings etc.
      /**
       * Listing 14-25: Adding a Click Listener to individual items within a Collection View Widget using a Pending Intent
       */
      Intent templateIntent = new Intent(Intent.ACTION_VIEW);      
      templateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
      PendingIntent templatePendingIntent = PendingIntent.getActivity(
        context, 0, templateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
      /** **/
            
      views.setPendingIntentTemplate(R.id.widget_stack_view, 
                                     templatePendingIntent);


      // Notify the App Widget Manager to update the widget using
      // the modified remote view.
      appWidgetManager.updateAppWidget(appWidgetId, views);
    }
  }
}