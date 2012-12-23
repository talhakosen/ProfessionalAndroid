package com.paad.PA4AD_Ch14_MyWidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.SystemClock;
import android.view.View;
import android.widget.RemoteViews;

public class FullAppWidget extends AppWidgetProvider {
  @Override
  public void onUpdate(Context context,
                       AppWidgetManager appWidgetManager,
                       int[] appWidgetIds) {
 
    RemoteViews views = new RemoteViews(context.getPackageName(),
        R.layout.full_widget_layout);
    
    Bitmap myBitmap = null;
    /**
     * Listing 14-6: Using a Remote View to apply methods to Views within an App Widget
     */
    // Set the image level for an ImageView.
    views.setInt(R.id.widget_image_view, "setImageLevel", 2);
    // Show the cursor of a TextView.
    views.setBoolean(R.id.widget_text_view, "setCursorVisible", true);
    // Assign a bitmap to an ImageButton.
    views.setBitmap(R.id.widget_image_button, "setImageBitmap", myBitmap);

    /**
     * Listing 14-7: Modifying View properties within an App Widget Remote View
     */
    // Update a Text View
    views.setTextViewText(R.id.widget_text, "Updated Text");
    views.setTextColor(R.id.widget_text, Color.BLUE);
    // Update an Image View
    views.setImageViewResource(R.id.widget_image, R.drawable.icon);
    // Update a Progress Bar
    views.setProgressBar(R.id.widget_progressbar, 100, 50, false);
    // Update a Chronometer
    views.setChronometer(R.id.widget_chronometer, SystemClock.elapsedRealtime(), null, true);


    views.setViewVisibility(R.id.widget_text, View.VISIBLE);

    appWidgetManager.updateAppWidget(appWidgetIds, views);
  }

  @Override
  public void onDeleted(Context context, int[] appWidgetIds) {
    // TODO Handle deletion of the widget.
    super.onDeleted(context, appWidgetIds);
  }

  @Override
  public void onDisabled(Context context) {
    // TODO Widget has been disabled.
    super.onDisabled(context);
  }

  @Override
  public void onEnabled(Context context) {
    // TODO Widget has been enabled.
    super.onEnabled(context);
  }
}