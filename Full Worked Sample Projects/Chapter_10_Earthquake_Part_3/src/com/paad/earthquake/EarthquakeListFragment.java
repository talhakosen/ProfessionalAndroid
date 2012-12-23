package com.paad.earthquake;

import java.util.Date;

import android.app.DialogFragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class EarthquakeListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
  
  SimpleCursorAdapter adapter;

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    // Create a new Adapter and bind it to the List View
    adapter = new SimpleCursorAdapter(getActivity(),
      android.R.layout.simple_list_item_1, null,
      new String[] { EarthquakeProvider.KEY_SUMMARY },
      new int[] { android.R.id.text1 }, 0);
    setListAdapter(adapter);

    getLoaderManager().initLoader(0, null, this);  

    Thread t = new Thread(new Runnable() {
      public void run() {
        refreshEarthquakes(); 
      }
    });
    t.start();
  }
  
  private static final String TAG = "EARTHQUAKE";
  
  Handler handler = new Handler();
  
  public void refreshEarthquakes() {
    handler.post(new Runnable() {
      public void run() {
        getLoaderManager().restartLoader(0, null, EarthquakeListFragment.this); 
      }
    });
    
    getActivity().startService(new Intent(getActivity(), 
                                          EarthquakeUpdateService.class));
  }


  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = new String[] {
      EarthquakeProvider.KEY_ID,
      EarthquakeProvider.KEY_SUMMARY
    }; 

    Earthquake earthquakeActivity = (Earthquake)getActivity();
    String where = EarthquakeProvider.KEY_MAGNITUDE + " > " + 
                   earthquakeActivity.minimumMagnitude;
   
    CursorLoader loader = new CursorLoader(getActivity(), 
      EarthquakeProvider.CONTENT_URI, projection, where, null, null);
    
    return loader;
  }

  public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    adapter.swapCursor(cursor);
  }

  public void onLoaderReset(Loader<Cursor> loader) {
    adapter.swapCursor(null);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    
    ContentResolver cr = getActivity().getContentResolver();
    
    Cursor result = 
      cr.query(ContentUris.withAppendedId(EarthquakeProvider.CONTENT_URI, id),
               null, null, null, null);
    
    if (result.moveToFirst()) {
      Date date = 
        new Date(result.getLong(
          result.getColumnIndex(EarthquakeProvider.KEY_DATE)));

      String details =       
        result.getString(
          result.getColumnIndex(EarthquakeProvider.KEY_DETAILS));

      double magnitude =
        result.getDouble(
          result.getColumnIndex(EarthquakeProvider.KEY_MAGNITUDE));

      String linkString =  
        result.getString(
          result.getColumnIndex(EarthquakeProvider.KEY_LINK));

      double lat = 
        result.getDouble(
          result.getColumnIndex(EarthquakeProvider.KEY_LOCATION_LAT));

      double lng = 
        result.getDouble(
          result.getColumnIndex(EarthquakeProvider.KEY_LOCATION_LNG));

      Location location = new Location("db");
      location.setLatitude(lat);
      location.setLongitude(lng);
      
      Quake quake = new Quake(date, details, location, magnitude, linkString);
      
      DialogFragment newFragment = EarthquakeDialog.newInstance(getActivity(), quake);
      newFragment.show(getFragmentManager(), "dialog");
    }
  }

}