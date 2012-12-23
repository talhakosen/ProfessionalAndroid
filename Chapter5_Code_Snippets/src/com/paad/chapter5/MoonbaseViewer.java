package com.paad.chapter5;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MoonbaseViewer extends Activity {
	
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
	  super.onCreateOptionsMenu(menu);
	  
	  // Create the intent used to resolve which actions 
	  // should appear in the menu.
	  Intent intent = new Intent();
	  //intent.setData(MoonBaseProvider.CONTENT_URI);
	  intent.addCategory(Intent.CATEGORY_SELECTED_ALTERNATIVE);
	      
	  // Normal menu options to let you set a group and ID
	  // values for the menu items you're adding.
	  int menuGroup = 0;
	  int menuItemId = 0;
	  int menuItemOrder = Menu.NONE;
	      
	  // Provide the name of the component that's calling
	  // the action -- generally the current Activity.
	  ComponentName caller = getComponentName();
	      
	  // Define intents that should be added first.
	  Intent[] specificIntents = null;	  
	  // The menu items created from the previous Intents
	  // will populate this array.
	  MenuItem[] outSpecificItems = null;
	   
	  // Set any optional flags.
	  int flags = Menu.FLAG_APPEND_TO_GROUP;
	    
	  // Populate the menu
	  menu.addIntentOptions(menuGroup, 
	                        menuItemId,
	                        menuItemOrder,
	                        caller, 
	                        specificIntents, 
	                        intent, 
	                        flags, 
	                        outSpecificItems);
	     
	  return true;
	} 

}