package com.paad.PA4AD_Ch14_MyWidget;

/**
 * Listing 14-21: Creating a Remote Views Service
 */
import java.util.ArrayList;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class MyRemoteViewsService extends RemoteViewsService {
  
  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new MyRemoteViewsFactory(getApplicationContext(), intent);
  }

  /**
   * Listing 14-23: Creating a Remote Views Factory
   */
  class MyRemoteViewsFactory implements RemoteViewsFactory {
    
    private ArrayList<String> myWidgetText = new ArrayList<String>();
    private Context context;
    private Intent intent;
    private int widgetId;
    
    public MyRemoteViewsFactory(Context context, Intent intent) {
      // Optional constructor implementation. 
      // Useful for getting references to the 
      // Context of the calling widget
      this.context = context;
      this.intent = intent;
      
      widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    // Set up any connections / cursors to your data source. 
    // Heavy lifting, like downloading data should be 
    // deferred to onDataSetChanged()or getViewAt(). 
    // Taking more than 20 seconds in this call will result
    // in an ANR.
    public void onCreate() {
      myWidgetText.add("The");
      myWidgetText.add("quick");
      myWidgetText.add("brown");
      myWidgetText.add("fox");
      myWidgetText.add("jumps");
      myWidgetText.add("over");
      myWidgetText.add("the");
      myWidgetText.add("lazy");
      myWidgetText.add("droid");
    }

    // Called when the underlying data collection being displayed is 
    // modified. You can use the AppWidgetManager's 
    // notifyAppWidgetViewDataChanged method to trigger this handler.
    public void onDataSetChanged() {
      // TODO Processing when underlying data has changed.
    }
    
    // Return the number of items in the collection being displayed.
    public int getCount() {
      return myWidgetText.size();
    }

    // Return true if the unique IDs provided by each item are stable -- 
    // that is, they don’t change at runtime.
    public boolean hasStableIds() {
      return false;
    }

    // Return the unique ID associated with the item at a given index.
    public long getItemId(int index) {
      return index;
    }

    // The number of different view definitions. Usually 1.
    public int getViewTypeCount() {
      return 1;
    }

    // Optionally specify a "loading" view to display. Return null to 
    // use the default.
    public RemoteViews getLoadingView() {
      return null;
    }

    // Create and populate the View to display at the given index.
    public RemoteViews getViewAt(int index) {
      // Create a view to display at the required index.
      RemoteViews rv = new RemoteViews(context.getPackageName(),
        R.layout.my_stack_widget_item_layout);
      
       // Populate the view from the underlying data.
      rv.setTextViewText(R.id.widget_title_text,
                         myWidgetText.get(index));
      rv.setTextViewText(R.id.widget_text, "View Number: " + 
                         String.valueOf(index));
      
      /**
       * Listing 14-26: Filling in a Pending Intent template for each item displayed in your Collection View Widget
       */
      // Create the item-specific fill-in Intent that will populate 
      // the Pending Intent template created in the App Widget Provider.
      Intent fillInIntent = new Intent();
      fillInIntent.putExtra(Intent.EXTRA_TEXT, myWidgetText.get(index));
      rv.setOnClickFillInIntent(R.id.widget_title_text, fillInIntent);
      
      return rv;
    }

    // Close connections, cursors, or any other persistent state you
    // created in onCreate.
    public void onDestroy() {
      myWidgetText.clear();
    }
  }
}
