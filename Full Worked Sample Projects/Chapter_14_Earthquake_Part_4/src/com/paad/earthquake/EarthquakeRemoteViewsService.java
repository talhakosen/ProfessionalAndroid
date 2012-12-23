package com.paad.earthquake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class EarthquakeRemoteViewsService extends RemoteViewsService {
  
  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new EarthquakeRemoteViewsFactory(getApplicationContext());
  }
  
  class EarthquakeRemoteViewsFactory implements RemoteViewsFactory {
    
    private Context context;

    public EarthquakeRemoteViewsFactory(Context context) {
      this.context = context;
    }

    private Cursor c;

    private Cursor executeQuery() {
      String[] projection = new String[] {
          EarthquakeProvider.KEY_ID,
          EarthquakeProvider.KEY_MAGNITUDE,
          EarthquakeProvider.KEY_DETAILS
        };
        
      Context appContext = getApplicationContext();
      SharedPreferences prefs = 
        PreferenceManager.getDefaultSharedPreferences(appContext);

      int minimumMagnitude = 
        Integer.parseInt(prefs.getString(PreferencesActivity.PREF_MIN_MAG, "3"));
      
      String where = EarthquakeProvider.KEY_MAGNITUDE + " > " + minimumMagnitude;
       
      return context.getContentResolver().query(EarthquakeProvider.CONTENT_URI, 
          projection, where, null, null);
    }

    public void onCreate() {
      c = executeQuery();
    } 
  
    public void onDataSetChanged() {
      c = executeQuery();
    }

    public void onDestroy() {
      c.close();
    }

    public int getCount() {
      if (c != null)
        return c.getCount();
      else
        return 0;
    }

    public long getItemId(int index) {
      if (c != null)
        return c.getLong(c.getColumnIndex(EarthquakeProvider.KEY_ID));
      else
        return index;  
    }
  
    public RemoteViews getViewAt(int index) {
      // Move the Cursor to the required index.
      c.moveToPosition(index);
      
      // Extract the values for the current cursor row.
      int idIdx = c.getColumnIndex(EarthquakeProvider.KEY_ID);
      int magnitudeIdx = c.getColumnIndex(EarthquakeProvider.KEY_MAGNITUDE);
      int detailsIdx = c.getColumnIndex(EarthquakeProvider.KEY_DETAILS);

      String id = c.getString(idIdx); 
      String magnitude = c.getString(magnitudeIdx); 
      String details = c.getString(detailsIdx); 

      // Create a new Remote Views object and use it to populate the 
      // layout used to represent each earthquake in the list.
      RemoteViews rv = new RemoteViews(context.getPackageName(), 
                                       R.layout.quake_widget);

      rv.setTextViewText(R.id.widget_magnitude, magnitude);
      rv.setTextViewText(R.id.widget_details, details);
          
      // Create the fill-in Intent that adds the URI for the current item
      // to the template Intent.
      Intent fillInIntent = new Intent();
      Uri uri = Uri.withAppendedPath(EarthquakeProvider.CONTENT_URI, id);
      fillInIntent.setData(uri);

      rv.setOnClickFillInIntent(R.id.widget_magnitude, fillInIntent);
      rv.setOnClickFillInIntent(R.id.widget_details, fillInIntent);
          
      return rv;
    }

    public int getViewTypeCount() {
      return 1;
    }
  
    public boolean hasStableIds() {
      return true;
    }

    public RemoteViews getLoadingView() {
      return null;
    }

  }
}