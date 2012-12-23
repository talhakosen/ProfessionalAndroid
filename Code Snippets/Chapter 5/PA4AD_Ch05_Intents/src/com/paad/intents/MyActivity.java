package com.paad.intents;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyActivity extends Activity {
  protected static final String TAG = "INTENT_ACTIVITY";
  final boolean somethingWeird = true;
  final boolean itDontLookGood = true;

  private void listing501() {
    /**
     *  Listing 5-1
     */
    Intent intent = new Intent(MyActivity.this, SelectHorseActivity.class);
    startActivity(intent); 
  }
  
  private void listing502() {
    /**
     * Listing 5-2: Implicitly starting an Activity
     */
    if (somethingWeird && itDontLookGood) {
      // Create the implicit Intent to use to start a new Activity.
      Intent intent = 
        new Intent(Intent.ACTION_DIAL, Uri.parse("tel:555-2368"));

      // Check if an Activity exists to perform this action.
      PackageManager pm = getPackageManager();
      ComponentName cn = intent.resolveActivity(pm);
      if (cn == null) {
        // If there is no Activity available to perform the action
        // Check to see if the Market is available.
        Uri marketUri =
          Uri.parse("market://search?q=pname:com.myapp.packagename");
        Intent marketIntent = new 
          Intent(Intent.ACTION_VIEW).setData(marketUri);

        // If the Market is available, use it to download an application
        // capable of performing the required action. Otherwise log an
        // error.
        if (marketIntent.resolveActivity(pm) != null)
          startActivity(marketIntent);
        else
          Log.d(TAG, "Market client not available.");
      } 
      else 
        startActivity(intent);
    } 
  }
  
  /**
   * Listing 5-3: Explicitly starting a sub-Activity for a result 
   */
  private static final int SHOW_SUBACTIVITY = 1;

  private void startSubActivity() {
    Intent intent = new Intent(this, MyOtherActivity.class);
    startActivityForResult(intent, SHOW_SUBACTIVITY);
  }
  
  /**
   * Listing 5-4: Implicitly starting a sub-Activity for a result 
   */
  private static final int PICK_CONTACT_SUBACTIVITY = 2;

  private void startSubActivityImplicitly() {
    Uri uri = Uri.parse("content://contacts/people");
    Intent intent = new Intent(Intent.ACTION_PICK, uri);
    startActivityForResult(intent, PICK_CONTACT_SUBACTIVITY);
  }
  
  /**
   *  Listing 5-6: Implementing an On Activity Result handler 
   */
  private static final int SELECT_HORSE = 1;
  private static final int SELECT_GUN = 2;

  Uri selectedHorse = null;
  Uri selectedGun = null;

  @Override
  public void onActivityResult(int requestCode, 
                               int resultCode,
                               Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    switch(requestCode) {
      case (SELECT_HORSE):
        if (resultCode == Activity.RESULT_OK)
          selectedHorse = data.getData();
        break;

      case (SELECT_GUN):
        if (resultCode == Activity.RESULT_OK)
          selectedGun = data.getData();
        break;

      default: break;
    }
  }
  
  /**
   * Listing 5-16: Generating a list of possible actions to be performed on specific data
   */
  private void listing516() {
    PackageManager packageManager = getPackageManager();
  
    // Create the intent used to resolve which actions
    // should appear in the menu.
    Intent intent = new Intent();
    intent.setData(MoonBaseProvider.CONTENT_URI);
    intent.addCategory(Intent.CATEGORY_SELECTED_ALTERNATIVE);
  
    // Specify flags. In this case, to return only filters
    // with the default category.
    int flags = PackageManager.MATCH_DEFAULT_ONLY;
     
    // Generate the list
    List<ResolveInfo> actions;
    actions = packageManager.queryIntentActivities(intent, flags);
  
    // Extract the list of action names
    ArrayList<String> labels = new ArrayList<String>();
    Resources r = getResources();
    for (ResolveInfo action : actions )
      labels.add(r.getString(action.labelRes));
    
    // ***
    
    // Print the found labels
    for (String label : labels)
      Log.d(TAG, label);
  }
  
  /**
   * Listing 5-17: Dynamic Menu population from advertised actions 
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);

    // Create the intent used to resolve which actions
    // should appear in the menu.
    Intent intent = new Intent();
    intent.setData(MoonBaseProvider.CONTENT_URI);
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
  
  //***
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
        
    Button buttonExplicitStart = (Button)findViewById(R.id.button1);
    buttonExplicitStart.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        listing501();
      }
    });

    Button buttonImplicitStart = (Button)findViewById(R.id.button2);
    buttonImplicitStart.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        listing502();
      }
    });
    
    Button buttonSubActivity = (Button)findViewById(R.id.button3);
    buttonSubActivity.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        startSubActivity(); 
      }
    });
    
    Button buttonSubActivityImplicitly = (Button)findViewById(R.id.button4);
    buttonSubActivityImplicitly.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        startSubActivityImplicitly(); 
      }
    });
    
    Button buttonThirdPartyActions = (Button)findViewById(R.id.button5);
    buttonThirdPartyActions.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        listing516();
      }
    });
  }
}