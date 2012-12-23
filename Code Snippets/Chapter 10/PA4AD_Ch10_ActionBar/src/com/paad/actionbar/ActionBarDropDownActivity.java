package com.paad.actionbar;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ActionBarDropDownActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    ActionBar actionBar = getActionBar();
    
    /**
     * Listing 10-10: Creating an Action Bar drop-down list
     */
    // Select the drop-down navigation mode.
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    
    // Create a new Spinner Adapter that contains the values to
    // be displayed in the drop down.
    ArrayAdapter dropDownAdapter = 
      ArrayAdapter.createFromResource(this,
                                      R.array.my_dropdown_values,
                                      android.R.layout.simple_list_item_1);
    
    // Assign the callbacks to handle drop-down selections.
    actionBar.setListNavigationCallbacks(dropDownAdapter, 
      new OnNavigationListener() {
        public boolean onNavigationItemSelected(int itemPosition, 
                                                long itemId) {
          // TODO Modify your UI based on the position 
          // of the drop down item selected.
          return true;
        }
     }); 
  }
}