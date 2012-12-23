package com.paad.PA4AD_Ch14_MyWidget;

/**
 * Listing 14-3: App Widget implementation
 */
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.widget.RemoteViews;
import android.content.Context;

public class SkeletonAppWidget extends AppWidgetProvider {
  @Override
  public void onUpdate(Context context,
                       AppWidgetManager appWidgetManager,
                       int[] appWidgetIds) {
    // TODO Update the Widget UI.
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
