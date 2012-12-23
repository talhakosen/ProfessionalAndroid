/*
 * Demonstrates each of the menu options available in Android 
 */

package com.paad.chapter4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.TextView;

public class A7MenuDemo extends Activity {
	
	// Static identifiers for each context menu and group.
	static final private int MENU_ITEM = Menu.FIRST;
	static final private int CHECKBOX_ITEM = Menu.FIRST+1;
	static final private int RADIOBUTTON_1 = Menu.FIRST+2;
	static final private int RADIOBUTTON_2 = Menu.FIRST+3;
	static final private int RADIOBUTTON_3 = Menu.FIRST+4;
	static final private int RB_GROUP = 1; 
	
	@Override
	public void onCreate(Bundle icicle) {
	  super.onCreate(icicle);
	  
	  // Create a new View
	  TextView view = new TextView(this);
	  view.setText("Long click for a context menu");	 
	  setContentView(view);	  
	  
	  // Assign it a context menu
	  registerForContextMenu(view);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	  super.onCreateOptionsMenu(menu);

	  // Group ID
	  int groupId = 0;
	  // Unique menu item identifier. Used for event handling.
	  int menuItemId = MENU_ITEM;
	  // The order position of the item
	  int menuItemOrder = Menu.NONE;
	  // Text to be displayed for this menu item.	 
	  int menuItemText = R.string.menu_item;

	  // Create a new menu item and keep a reference to it.
	  MenuItem menuItem = menu.add(groupId, menuItemId, menuItemOrder, menuItemText);
	  menuItem.setShortcut('0', 'b');
	  menuItem.setTitleCondensed("Short Menu Title");
	  menuItem.setIcon(android.R.drawable.btn_star).setCheckable(true);
	  menuItem.setIntent(new Intent(this, A6CustomView.class)); 
	  menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		public boolean onMenuItemClick(MenuItem menuItem) {
			// TODO React to menu item click
			
			// Return true if click has been handled.
			return false;
		}		  
	  });
	       
	  // Added extra items to make sure there's more than six to 
	  // force the extended menu to appear.
	  menu.add(groupId, menuItemId+5, menuItemOrder, "Menu Item 2");
	  menu.add(groupId, menuItemId+6, menuItemOrder, "Menu Item 3");
	  menu.add(groupId, menuItemId+7, menuItemOrder, "Menu Item 4");
	  menu.add(groupId, menuItemId+8, menuItemOrder, "Menu Item 5");
	  menu.add(groupId, menuItemId+10, menuItemOrder, "Menu Item Extended Menu").setIcon(android.R.drawable.btn_star);
	  
      // Create a new check box item.
	  menu.add(0, CHECKBOX_ITEM, Menu.NONE, "CheckBox").setCheckable(true).setChecked(true).setIcon(android.R.drawable.btn_star);

	  // Create a radio button group.
	  menu.add(RB_GROUP, RADIOBUTTON_1, Menu.NONE, "Radiobutton 1");
	  menu.add(RB_GROUP, RADIOBUTTON_2, Menu.NONE, "Radiobutton 2");
	  menu.add(RB_GROUP, RADIOBUTTON_3, Menu.NONE, "Radiobutton 3").setChecked(true);
	  menu.setGroupCheckable(RB_GROUP, true, true);

      // Add a new sub menu to the main options menu.
	  int group = 0;
	  int id = 20;
	  SubMenu sub = menu.addSubMenu(group, id, Menu.NONE, "Submenu");
	  sub.setHeaderIcon(android.R.drawable.btn_star);
	  sub.setIcon(R.drawable.icon);
	  
	  MenuItem submenuItem = sub.add(groupId, menuItemId, menuItemOrder, menuItemText);
	  submenuItem.setShortcut('0', 'b');
	  submenuItem.setTitleCondensed("Short Menu Title");
	  submenuItem.setIcon(android.R.drawable.btn_star);	  
	  sub.add(0, 0, 0, "Check Box").setCheckable(true);	  
	  sub.add(1, 0, 0, "Radio 1").setChecked(true);
	  sub.add(1, 0, 0, "Radio 2");
	  sub.add(1, 0, 0, "Radio 3");
	  sub.setGroupCheckable(1, true, true);	  	 	  

	  return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	  super.onPrepareOptionsMenu(menu);

	  MenuItem menuItem = menu.findItem(MENU_ITEM);
	  
	  menuItem.setTitleCondensed("Short Text");
	  //[ ... modify menu items ... ]

	  return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		  super.onOptionsItemSelected(item);
		   
		  // Find which menu item has been selected
		  switch (item.getItemId()) {

		    // Check for each known menu item
		    case (MENU_ITEM+5): 
		      //[ ... Perform menu handler actions ... ]
		      return true;
		  }

		  // Return false if you have not handled the menu item.
		  return false;
		}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) { 
	  super.onContextItemSelected(item);
	  return false;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
	  super.onCreateContextMenu(menu, v, menuInfo);
	  
	    menu.setHeaderTitle("Context Menu");	    
	    menu.add(0, Menu.FIRST, Menu.NONE, "Context Item 1").setIcon(R.drawable.menu_item);
	    menu.add(0, Menu.FIRST+1, Menu.NONE, "Context Item 2").setCheckable(true);
	    menu.add(0, Menu.FIRST+2, Menu.NONE, "Context Item 3").setShortcut('3', '3');
	    SubMenu sub = menu.addSubMenu("submenu");
	    sub.add("Sub Menu Item");

	}
}