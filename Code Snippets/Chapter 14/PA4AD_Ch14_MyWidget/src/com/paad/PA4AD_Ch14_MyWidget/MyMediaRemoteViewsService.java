package com.paad.PA4AD_Ch14_MyWidget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class MyMediaRemoteViewsService extends RemoteViewsService {
  
  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new MyRemoteViewsFactory(getApplicationContext());
  }
  
  /**
   * Listing 14-27: Creating a Content Provider-backed Remote Views Factory
   */
  class MyRemoteViewsFactory implements RemoteViewsFactory {
    
    private Context context;
    private ContentResolver cr;
    private Cursor c;
    
    public MyRemoteViewsFactory(Context context) {
      // Get a reference to the application context and
      // its Content Resolver.
      this.context = context;
      cr = context.getContentResolver();
    }

    public void onCreate() {
      // Execute the query that returns a Cursor over the data
      // to be displayed. Any secondary lookups or decoding should
      // be completed in the onDataSetChanged handler.
      c = cr.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, 
                   null, null, null, null);
    }

    public void onDataSetChanged() {
      // Any secondary lookups, processing, or decoding can be done
      // here synchronously. The Widget will be updated only after
      // this method has completed.
    }
    
    public int getCount() {
      // Return the number of items in the Cursor.
      if (c != null)
        return c.getCount();
      else
        return 0;
    }

    public long getItemId(int index) {
      // Return the unique ID associated with a particular item.
      if (c != null)
        return c.getInt(
          c.getColumnIndex(MediaStore.Images.Thumbnails._ID));
      else
        return index;
    }

    public RemoteViews getViewAt(int index) {
      // Move the Cursor to the requested row position.
      c.moveToPosition(index);

      // Extract the data from the required columns.
      int idIdx = c.getColumnIndex(MediaStore.Images.Thumbnails._ID);
      String id = c.getString(idIdx);
      Uri uri = Uri.withAppendedPath(
          MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, ""
          + id);
      
      // Create a new Remote Views object using the appropriate 
      // item layout
      RemoteViews rv = new RemoteViews(context.getPackageName(),
        R.layout.my_media_widget_item_layout);
      
      // Assign the values extracted from the Cursor to the Remote Views. 
      rv.setImageViewUri(R.id.widget_media_thumbnail, uri);

      // Assign the item-specific fill-in Intent that will populate 
      // the Pending Intent template specified in the App Widget
      // Provider. In this instance the template Intent specifies 
      // an ACTION_VIEW action.
      Intent fillInIntent = new Intent();
      fillInIntent.setData(uri);
      rv.setOnClickFillInIntent(R.id.widget_media_thumbnail, 
                                fillInIntent);
      
      return rv;
    }

    public int getViewTypeCount() {
      // The number of different view definitions to use.
      // For Content Providers, this will almost always be 1.
      return 1;
    }

    public boolean hasStableIds() {
      // Content Provider IDs should be unique and permanent.
      return true;
    }

    public void onDestroy() {
      // Close the result Cursor.
      c.close();
    }

    public RemoteViews getLoadingView() {
      // Use the default loading view.
      return null;
    }
  } 
}
