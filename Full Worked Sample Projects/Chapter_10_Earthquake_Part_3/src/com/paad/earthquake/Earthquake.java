package com.paad.earthquake;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

public class Earthquake extends Activity {
  
  TabListener<EarthquakeListFragment> listTabListener;
  TabListener<EarthquakeMapFragment> mapTabListener;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    updateFromPreferences();
    
    ActionBar actionBar = getActionBar();

    View fragmentContainer = findViewById(R.id.EarthquakeFragmentContainer); 

    // Use tablet navigation if the list and map fragments are both available.
    boolean tabletLayout = fragmentContainer == null;
    
    // If it's not a tablet, use the tab Action Bar navigation.
    if (!tabletLayout) {
      actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
      actionBar.setDisplayShowTitleEnabled(false);
            
      // Create and add the list tab.
      Tab listTab = actionBar.newTab();

      listTabListener = new TabListener<EarthquakeListFragment>
        (this, R.id.EarthquakeFragmentContainer, EarthquakeListFragment.class);

      listTab.setText("List")
             .setContentDescription("List of earthquakes")
             .setTabListener(listTabListener);

      actionBar.addTab(listTab);

      // Create and add the map tab.
      Tab mapTab = actionBar.newTab();

      mapTabListener = new TabListener<EarthquakeMapFragment>
        (this, R.id.EarthquakeFragmentContainer, EarthquakeMapFragment.class);

      mapTab.setText("Map")
            .setContentDescription("Map of earthquakes")
            .setTabListener(mapTabListener);

      actionBar.addTab(mapTab);
    }
  }

  private static final int SHOW_PREFERENCES = 1;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);

    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    
    // Moved from onCreate -- Retreive the Search View and configure/enable it.
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
      case (R.id.menu_refresh): {
        startService(new Intent(this, EarthquakeUpdateService.class));
        return true;
      }
      case (R.id.menu_preferences): {
        Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?    
          PreferencesActivity.class : FragmentPreferences.class;
        Intent i = new Intent(this, c);
          
        startActivityForResult(i, SHOW_PREFERENCES);   
        return true;
      }
      default: return false;
    }
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

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    
    if (requestCode == SHOW_PREFERENCES) {
      updateFromPreferences();   
      startService(new Intent(this, EarthquakeUpdateService.class));
    }
  }
  
  private static String ACTION_BAR_INDEX = "ACTION_BAR_INDEX";

  @Override
  public void onSaveInstanceState(Bundle outState) {
    View fragmentContainer = findViewById(R.id.EarthquakeFragmentContainer); 
    boolean tabletLayout = fragmentContainer == null;
      
    if (!tabletLayout) {
      // Save the current Action Bar tab selection
      int actionBarIndex = getActionBar().getSelectedTab().getPosition();
      SharedPreferences.Editor editor = getPreferences(Activity.MODE_PRIVATE).edit();
      editor.putInt(ACTION_BAR_INDEX, actionBarIndex);
      editor.apply();
      
      // Detach each of the Fragments
      FragmentTransaction ft = getFragmentManager().beginTransaction();
      if (mapTabListener.fragment != null)
        ft.detach(mapTabListener.fragment);
      if (listTabListener.fragment != null)
        ft.detach(listTabListener.fragment);
      ft.commit();
    } 

    super.onSaveInstanceState(outState);
  }

  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);

    View fragmentContainer = findViewById(R.id.EarthquakeFragmentContainer); 
    boolean tabletLayout = fragmentContainer == null;
      
    if (!tabletLayout) {
      // Find the recreated Fragments and assign them to their associated Tab Listeners.
      listTabListener.fragment = 
        getFragmentManager().findFragmentByTag(EarthquakeListFragment.class.getName());
      mapTabListener.fragment =
        getFragmentManager().findFragmentByTag(EarthquakeMapFragment.class.getName());

      // Restore the previous Action Bar tab selection.    
      SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
      int actionBarIndex = sp.getInt(ACTION_BAR_INDEX, 0);
      getActionBar().setSelectedNavigationItem(actionBarIndex);
    }
  }
  
  @Override 
  public void onResume() {
    super.onResume();
    View fragmentContainer = findViewById(R.id.EarthquakeFragmentContainer); 
    boolean tabletLayout = fragmentContainer == null;
      
    if (!tabletLayout) {
      SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
      int actionBarIndex = sp.getInt(ACTION_BAR_INDEX, 0);
      getActionBar().setSelectedNavigationItem(actionBarIndex);
    }
  }
  
  public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
  
    private Fragment fragment;
    private Activity activity;
    private Class<T> fragmentClass;
    private int fragmentContainer;
  
    public TabListener(Activity activity, int fragmentContainer, 
                       Class<T> fragmentClass) {
  
      this.activity = activity;
      this.fragmentContainer = fragmentContainer;
      this.fragmentClass = fragmentClass;
    }
  
    // Called when a new tab has been selected
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
      if (fragment == null) {
        String fragmentName = fragmentClass.getName();
        fragment = Fragment.instantiate(activity, fragmentName);
        ft.add(fragmentContainer, fragment, fragmentName);
      } else
        ft.attach(fragment);
    }
  
    // Called on the currently selected tab when a different tag is
    // selected. 
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
      if (fragment != null)
        ft.detach(fragment);
    } 
  
    // Called when the selected tab is selected.
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
      if (fragment != null)
        ft.attach(fragment);
    }
  }

}