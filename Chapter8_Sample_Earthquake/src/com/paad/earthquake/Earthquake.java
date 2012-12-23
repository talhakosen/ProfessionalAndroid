package com.paad.earthquake;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Earthquake extends Activity {
  private static final int QUAKE_DIALOG = 1;
  
  private static final int MENU_UPDATE = Menu.FIRST;
  private static final int MENU_PREFERENCES = Menu.FIRST+1;
  
  private static final int SHOW_PREFERENCES = 1;
  private static final int MENU_EARTHQUAKE_MAP = Menu.FIRST+2;
  
  ListView earthquakeListView;
  
  ArrayList<Quake> earthquakes = new ArrayList<Quake>();
  ArrayAdapter<Quake> aa;
  
  Quake selectedQuake;

  EarthquakeReceiver receiver;

  // Preference values
  int minimumMagnitude = 0;
  boolean autoUpdate = false;
  int updateFreq = 0;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.main);

    earthquakeListView = (ListView)this.findViewById(R.id.earthquakeListView);
    earthquakeListView.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> _av, View _v, int _index, long _id) {
        selectedQuake = earthquakes.get(_index);
        showDialog(QUAKE_DIALOG);
      }
    });

    int layoutID = android.R.layout.simple_list_item_1;
    aa = new ArrayAdapter<Quake>(this, layoutID , earthquakes);
    earthquakeListView.setAdapter(aa);

    loadQuakesFromProvider();
    
    updateFromPreferences();
    refreshEarthquakes();  
  }  

  /** Refresh data from the earthquake feed using the Service*/
  private void refreshEarthquakes() {
	  startService(new Intent(this, EarthquakeService.class));
  }
  
  /** Add a new quake to the ArrayList */
	private void addQuakeToArray(Quake _quake) {
	  if (_quake.getMagnitude() > minimumMagnitude) {
	    // Add the new quake to our list of earthquakes.
	    earthquakes.add(_quake);

	    // Notify the array adapter of a change.
	    aa.notifyDataSetChanged();
	  }
	}

  /** Retrieve the Quakes from provider and populate the ArrayList */
  private void loadQuakesFromProvider() {
  	// Clear the existing earthquake array
  	earthquakes.clear();

	  ContentResolver cr = getContentResolver();

	  // Return all the saved earthquakes
	  Cursor c = cr.query(EarthquakeProvider.CONTENT_URI, null, null, null, null);
	 
	  if (c.moveToFirst())
	    {
	      do { 
	        // Extract the quake details.
	        Long datems = c.getLong(EarthquakeProvider.DATE_COLUMN);
	        String details = c.getString(EarthquakeProvider.DETAILS_COLUMN);
	        Float lat = c.getFloat(EarthquakeProvider.LATITUDE_COLUMN);
	        Float lng = c.getFloat(EarthquakeProvider.LONGITUDE_COLUMN);
	        Double mag = c.getDouble(EarthquakeProvider.MAGNITUDE_COLUMN);
	        String link = c.getString(EarthquakeProvider.LINK_COLUMN);

	        Location location = new Location("dummy");
	        location.setLongitude(lng);
	        location.setLatitude(lat);

	        Date date = new Date(datems);

	        Quake q = new Quake(date, details, location, mag, link);
	        addQuakeToArray(q);
	      } while(c.moveToNext());
	  }
	  c.close();
	}

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {     
    super.onCreateOptionsMenu(menu);

    menu.add(0, MENU_UPDATE, Menu.NONE, R.string.menu_update);
    menu.add(0, MENU_PREFERENCES, Menu.NONE, R.string.menu_preferences);  
    menu.add(0, MENU_EARTHQUAKE_MAP, Menu.NONE, 
                R.string.menu_earthquake_map).setIntent(new Intent(getApplicationContext(), EarthquakeMap.class));
    return true;
  }
             
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);
         
    switch (item.getItemId()) {
      case (MENU_UPDATE): {
        refreshEarthquakes();
        return true; 
      }
      case (MENU_PREFERENCES): {
        Intent i = new Intent(this, Preferences.class);
        startActivityForResult(i, SHOW_PREFERENCES);
        return true;
      }
    } 
    return false;
  }
  
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == SHOW_PREFERENCES)
      if (resultCode == Activity.RESULT_OK) {
        updateFromPreferences();
        refreshEarthquakes();
      } 
  }

  /** Update preference variables based on saved preferences */
  private void updateFromPreferences() {
    SharedPreferences prefs = getSharedPreferences(Preferences.USER_PREFERENCE, Activity.MODE_PRIVATE);

    int minMagIndex = prefs.getInt(Preferences.PREF_MIN_MAG, 0);
    if (minMagIndex < 0)
      minMagIndex = 0;

    int freqIndex = prefs.getInt(Preferences.PREF_UPDATE_FREQ, 0);
    if (freqIndex < 0)
      freqIndex = 0;

    autoUpdate = prefs.getBoolean(Preferences.PREF_AUTO_UPDATE, false);

    Resources r = getResources();
    // Get the option values from the arrays.
    int[] minMagValues = r.getIntArray(R.array.magnitude);
    int[] freqValues = r.getIntArray(R.array.update_freq_values);

    // Convert the values to ints.
    minimumMagnitude = minMagValues[minMagIndex];
    updateFreq = freqValues[freqIndex];
  }
    
  @Override
  public Dialog onCreateDialog(int id) {
    switch(id) {
      case (QUAKE_DIALOG) :        
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View quakeDetailsView = li.inflate(R.layout.quake_details, null);
    
        AlertDialog.Builder quakeDialog = new AlertDialog.Builder(getApplicationContext());
        quakeDialog.setTitle("Quake Time");         
        quakeDialog.setView(quakeDetailsView);
        return quakeDialog.create();
    }
    return null;
  }
     
  @Override
  public void onPrepareDialog(int id, Dialog dialog) {
    switch(id) {
      case (QUAKE_DIALOG) :                  
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateString = sdf.format(selectedQuake.getDate()); 
        String quakeText = "Mangitude " + selectedQuake.getMagnitude() + "\n" 
                           + selectedQuake.getDetails()  + "\n" + selectedQuake.getLink();          
        
        AlertDialog quakeDialog = (AlertDialog)dialog;
        quakeDialog.setTitle(dateString);
    
        TextView tv = (TextView)quakeDialog.findViewById(R.id.quakeDetailsTextView);
        if (tv != null)
          tv.setText(quakeText);

        break;
    }
  }
    
  @Override 
  public void onResume() {
    super.onResume();
    IntentFilter filter = new IntentFilter(EarthquakeService.NEW_EARTHQUAKE_FOUND);
    receiver = new EarthquakeReceiver();
    registerReceiver(receiver, filter);
    loadQuakesFromProvider();
  }

  @Override
  public void onPause() {
    unregisterReceiver(receiver);
    super.onPause();
  }
    
  public class EarthquakeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      loadQuakesFromProvider();
    }
  }
}