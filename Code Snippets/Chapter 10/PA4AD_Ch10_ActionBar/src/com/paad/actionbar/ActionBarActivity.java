package com.paad.actionbar;

import java.io.File;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ShareActionProvider;

public class ActionBarActivity extends Activity {
  
  protected static final String TAG = "ActionBarTabActivity";
  protected static final int POPUP_ITEM_1 = Menu.FIRST;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    /**
     * Listing 10-4: Customizing the Action Bar background
     */
    ActionBar actionBar = getActionBar();
    Resources r = getResources();

    Drawable myDrawable = r.getDrawable(R.drawable.gradient_header);

    actionBar.setBackgroundDrawable(myDrawable);

    /**
     * Listing 10-3: Customizing the Action Bar titles
     */
    actionBar.setSubtitle("Inbox");
    actionBar.setTitle("Label:important");
    
    /**
     *  Listing 10-19: Assigning a Context Menu to a View
     */
    EditText view = new EditText(this);
    setContentView(view);

//    registerForContextMenu(view);
    
    //
    View button = view;
    
    /**
     * Listing 10-20: Assigning a Popup Menu to a View
     */
    final PopupMenu popupMenu = new PopupMenu(this, button);

    popupMenu.inflate(R.menu.my_popup_menu);
    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
          case (POPUP_ITEM_1) :
            // TODO Handle popup menu clicks.
            return true;
          default: return false;
        }
      }
    });
    
    //
    View myView = view;
    myView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    
    /**
     * Listing 10-21: Reacting to changes in system UI visibility
     */
    myView.setOnSystemUiVisibilityChangeListener(
      new OnSystemUiVisibilityChangeListener() {
      
      public void onSystemUiVisibilityChange(int visibility) {
        if (visibility == View.SYSTEM_UI_FLAG_VISIBLE) {
          // TODO Display Action Bar and Status Bar
          getActionBar().show();
        }
        else {
          // TODO Hide Action Bar and Status Bar
          getActionBar().hide();
        }
      }
    });
  }
  
  /**
   * Listing 10-6: Handling application icon clicks
   * Listing 10-18: Handling Menu Item selections
   */
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    // Find which Menu Item has been selected
    switch (item.getItemId()) {
      // Check for each known Menu Item
      case (android.R.id.home) :
        Intent intent = new Intent(this, ActionBarActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
      case (MENU_ITEM):
        // [ ... Perform menu handler actions ... ]
        return true;

      // Return false if you have not handled the Menu Item
      default: return false;
    }
  }

  /**
   * Listing 10-11: Adding a Menu Item
   */
  static final private int MENU_ITEM = Menu.FIRST;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);

    // Group ID
    int groupId = 0;
    // Unique Menu Item identifier. Used for event handling
    int menuItemId = MENU_ITEM;
    // The order position of the item
    int menuItemOrder = Menu.NONE;
    // Text to be displayed for this Menu Item
    int menuItemText = R.string.menu_item;

    // Create the Menu Item and keep a reference to it
    MenuItem menuItem = menu.add(groupId, menuItemId,
                                 menuItemOrder, menuItemText);
    
    //
    menuItem = menu.add(groupId, menuItemId++, menuItemOrder, "Action Bar Item");
    
    /**
     * Listing 10-12: Making a Menu Item an action
     */
    menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
                             MenuItem.SHOW_AS_ACTION_WITH_TEXT);

    //
    menuItem = menu.add(groupId, menuItemId++, menuItemOrder, "ActionView Item");
    
    /**
     * Listing 10-13: Adding an action View
     */
    menuItem.setActionView(R.layout.my_action_view)
      .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM|
       MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

    //
    View myView = menuItem.getActionView();
    Button button = (Button)myView.findViewById(R.id.goButton);

    button.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        // TODO React to the button press.
        Log.d(TAG, "ActionView Button Pressed");
      }
    });
    
    menuItem = menu.add(groupId, menuItemId++, menuItemOrder, "Share Action Item");
    
    /**
     * Listing 10-14: Adding a Share Action Provider to a menu
     */
    // Create the sharing Intent
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("image/*");
    Uri uri = Uri.fromFile(new File(getFilesDir(), "test_1.jpg"));
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri.toString());

    ShareActionProvider shareProvider = new ShareActionProvider(this);
    shareProvider.setShareIntent(shareIntent);
       
    menuItem.setActionProvider(shareProvider)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

    return true;
  }
  
//  /**
//   * Listing 10-16: Inflating an XML menu resource
//   */
//  public boolean onCreateOptionsMenu(Menu menu) {
//    super.onCreateOptionsMenu(menu); 
//
//    MenuInflater inflater = getMenuInflater();
//    inflater.inflate(R.menu.my_menu, menu);
//
//    return true;
//  }
  
  /**
   * Listing 10-17: Modifying Menu Items dynamically
   */
  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);

    MenuItem menuItem = menu.findItem(MENU_ITEM);

    // [ ... modify Menu Items ... ]

    return true;
  }
}