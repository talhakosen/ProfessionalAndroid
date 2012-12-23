package com.paad.earthquake;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

public class Earthquake extends Activity {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    updateFromPreferences();
    
    // Use the Search Manager to find the SearchableInfo related to this 
    // Activity.
    SearchManager searchManager = 
      (SearchManager)getSystemService(Context.SEARCH_SERVICE);
    SearchableInfo searchableInfo = 
      searchManager.getSearchableInfo(getComponentName());

    // Bind the Activity's SearchableInfo to the Search View
    SearchView searchView = (SearchView)findViewById(R.id.searchView);
    searchView.setSearchableInfo(searchableInfo);
  }

  static final private int MENU_PREFERENCES = Menu.FIRST+1;
  static final private int MENU_UPDATE = Menu.FIRST+2;
  private static final int SHOW_PREFERENCES = 1;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);

    menu.add(0, MENU_PREFERENCES, Menu.NONE, R.string.menu_preferences);

    return true;
  }

  public boolean onOptionsItemSelected(MenuItem item){
    super.onOptionsItemSelected(item);
    switch (item.getItemId()) {

      case (MENU_PREFERENCES): {
        Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?    
          PreferencesActivity.class : FragmentPreferences.class;
        Intent i = new Intent(this, c);

        startActivityForResult(i, SHOW_PREFERENCES);
        return true;
      }
    }
    return false;
  }

  
  public int minimumMagnitude = 0;
  public boolean autoUpdateChecked = false;
  public int updateFreq = 0;

  private void updateFromPreferences() {
    Context context = getApplicationContext();
    SharedPreferences prefs = 
      PreferenceManager.getDefaultSharedPreferences(context);

    minimumMagnitude = 
      Integer.parseInt(prefs.getString(PreferencesActivity.PREF_MIN_MAG, "3"));
    updateFreq = 
      Integer.parseInt(prefs.getString(PreferencesActivity.PREF_UPDATE_FREQ, "60"));

    autoUpdateChecked = prefs.getBoolean(PreferencesActivity.PREF_AUTO_UPDATE, false);
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == SHOW_PREFERENCES)
      updateFromPreferences();
      
      FragmentManager fm = getFragmentManager();
      EarthquakeListFragment earthquakeList = 
          (EarthquakeListFragment)fm.findFragmentById(R.id.EarthquakeListFragment);
      earthquakeList.refreshEarthquakes();
  }


}