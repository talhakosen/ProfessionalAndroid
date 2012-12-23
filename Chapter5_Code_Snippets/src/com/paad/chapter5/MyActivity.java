/**
 * This Activity includes each of the code snippets used to demonstrate
 * Android functionality in Chapter 5.
 */

package com.paad.chapter5;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends Activity {
  
  public static final String NEW_LIFEFORM_DETECTED = "com.paad.action.NEW_LIFEFORM";

  private static final int SHOW_SUBACTIVITY = 1;
  private static final int PICK_CONTACT_SUBACTIVITY = 2;
	  
  private static final int TIME_DIALOG = 1;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.main);
            
    /** ---- Linkify Snippets ---- */

    TextView myTextView = (TextView)findViewById(R.id.myTextView);
    Linkify.addLinks(myTextView, Linkify.WEB_URLS|Linkify.EMAIL_ADDRESSES);
    
    int flags = Pattern.CASE_INSENSITIVE;
    Pattern pattern = Pattern.compile("\\bquake[0-9]*\\b", flags);
    Linkify.addLinks(myTextView, pattern, "content://com.pad.earthquake/earthquakes/");

    String prefixWith = "http://www.";        
    pattern = Pattern.compile("\\bTEST.COM\\b");
    Linkify.addLinks(myTextView, pattern, prefixWith, 
                     new MatchFilter() {
                       public boolean acceptMatch(CharSequence s, int start, int end) {
    	                   return (start == 0 || s.charAt(start-1) != '!');
                       }
                     }, 
                     new TransformFilter() {
                       public String  transformUrl(Matcher match, String url) {
            			       return url.toLowerCase();
            		       }
            	       });
          
    /** ---- Broadcast Receiver Snippets ---- */
    
    // Create and register the broadcast receiver.
    IntentFilter filter = new IntentFilter(NEW_LIFEFORM_DETECTED);
    InternalLifeformDetectedBroadcastReceiver receiver = new InternalLifeformDetectedBroadcastReceiver();
    registerReceiver(receiver, filter);
    
    // Unregister the receiver.
    unregisterReceiver(receiver);

    /** ---- Adapter Snippets ---- */

    ListView myListView = (ListView)findViewById(R.id.myListView);
  
    // Array Adapter
    ArrayList<String> myStringArray = new ArrayList<String>();
    ArrayAdapter<String> myAdapterInstance;

    myAdapterInstance = new ArrayAdapter<String>(getApplicationContext(), 
                                                 android.R.layout.simple_list_item_1, 
                                                 myStringArray);
    myListView.setAdapter(myAdapterInstance);
    myAdapterInstance.add("New Item Added");
    
    // Simple Cursor Adapter
    ListView scListView = (ListView)findViewById(R.id.scListView);
    
    String uriString = "content://contacts/people/";
    
    Cursor myCursor = managedQuery(Uri.parse(uriString), null, null, null, null);
    String[] fromColumns = new String[] {People.NUMBER, People.NAME};
    int[] toLayoutIDs = new int[] { R.id.nameTextView, R.id.numberTextView};

    SimpleCursorAdapter myAdapter;
    myAdapter = new SimpleCursorAdapter(getApplicationContext(),
                                        R.layout.simplecursorlayout,
                                        myCursor,
                                        fromColumns,
                                        toLayoutIDs);
    scListView.setAdapter(myAdapter);

    /** ---- Internet Access Snippets ---- */

    String myFeed = getString(R.string.my_feed);
    URL url;
    
    try {
      url = new URL(myFeed);
      URLConnection connection = url.openConnection(); 
         
      HttpURLConnection httpConnection = (HttpURLConnection)connection; 
      int responseCode = httpConnection.getResponseCode(); 
  
      if (responseCode == HttpURLConnection.HTTP_OK) { 
        InputStream in = httpConnection.getInputStream();
        // TODO: Process the input stream as required
      }
    } 
    catch (MalformedURLException e) {} 
    catch (IOException e) {}
  }
    
  @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	  super.onCreateOptionsMenu(menu);

	  // Each menu item demonstrates an alternative technique for
	  // using Intents to start Activities.
	   
	  // Explicit Activity Start
	  MenuItem menuItem = menu.add(0, 0, Menu.NONE, "Explicit Start");	  
	  menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
  	  public boolean onMenuItemClick(MenuItem menuItem) {
			  Intent intent = new Intent(getApplicationContext(), MyOtherActivity.class);
			  startActivity(intent);
			  return true;
	  	}  
	  });	  
	  
	  // Implicit Activity Start
	  MenuItem menuItem1 = menu.add(0, 0, Menu.NONE, "Implicit Start");	  
	  menuItem1.setOnMenuItemClickListener(new OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem menuItem) {
			  boolean somethingWeird = true;
			  boolean itDontLookGood = true;
			
			  if (somethingWeird && itDontLookGood) {
				  Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:555-2368"));
				  startActivity(intent);
		    }
			  
			  return true;
		  }
	  });
	  
    // Execute the same pattern as when a Linkified link is selected
	  menu.add(0, 0, Menu.NONE, "As Per Linkify").setOnMenuItemClickListener(new OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem menuItem) {
		    Uri uri = Uri.parse("tel:555-2368");
			  startActivity(new Intent(Intent.ACTION_VIEW, uri));
			  return true;
		  }
	  });
	  
	  // Explicitly start an Activity for a result (sub-Activity)
	  menu.add(0, 0, Menu.NONE, "Explicit SubActivity").setOnMenuItemClickListener(new OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem menuItem) {
				Intent intent = new Intent(getApplicationContext(), MyOtherActivity.class);
				intent.setAction("Action");
				intent.setData(Uri.parse("http://data"));
				// Result returned in onActivityResult
				startActivityForResult(intent, SHOW_SUBACTIVITY);
				return true;
			}
    });
	  
    // Implicitly start an Activity for a result (sub-Activity)
	  menu.add(0, 0, Menu.NONE, "Implicit SubActivity").setOnMenuItemClickListener(new OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem menuItem) {
				Uri uri = Uri.parse("content://contacts/people");
				Intent intent = new Intent(Intent.ACTION_PICK, uri);
				// Result resturned in onActivityResult
				startActivityForResult(intent, PICK_CONTACT_SUBACTIVITY);
				return true;
			}
    });
	  
    // Broadcast an Intent to be received by a Broadcast Receiver
	  menu.add(0, 0, Menu.NONE, "Broadcast").setOnMenuItemClickListener(new OnMenuItemClickListener() { 
			public boolean onMenuItemClick(MenuItem menuItem) {
        String lifeformType = "alien";
			  float currentLongitude = 0;
			  float currentLatitude = 0;
			    
			  Intent intent = new Intent(NEW_LIFEFORM_DETECTED);
			  intent.putExtra("type", lifeformType);
			  intent.putExtra("longitude", currentLongitude);
			  intent.putExtra("latitude", currentLatitude);

			  sendBroadcast(intent);
			  
			  return true;
			}  
    });
	  
		// Explicitly create and display a new Dialog box
	  menu.add(0, 0, Menu.NONE, "Dialog").setOnMenuItemClickListener(new OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem menuItem) {
			  
			  Dialog d = new Dialog(getApplicationContext());

  			// Have the new window tint and blur the window it obscures.
	  		Window window = d.getWindow();
		  	window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
			                  WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
 
	  		d.setTitle("Dialog Title");
		  	d.setContentView(R.layout.dialog_view);

			  TextView text = (TextView)d.findViewById(R.id.dialogTextView);
		  	text.setText("This is the text in my dialog");

		  	d.show();

        return true;
      }  
	  });
	  
  	// Explicitly create and display a new Alert Dialog box
	  menu.add(0, 0, Menu.NONE, "Alert Dialog").setOnMenuItemClickListener(new OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem menuItem) {			
			  Context context = getApplicationContext();
			  String title = "It is pitch black.";
			  String message = "You are likely to be eaten by a grue.";
			  String button1String = "Go Back";
			  String button2String = "Move Forward";

			  AlertDialog.Builder ad = new AlertDialog.Builder(context);
			  ad.setTitle(title);
			  ad.setMessage(message);
			  
			  ad.setPositiveButton(button1String, new OnClickListener() {
          public void onClick(DialogInterface dialog, int arg1) {
			      eatenByGrue();
			    }
          
          private void eatenByGrue() {}
			  });
			  
			  ad.setNegativeButton(button2String, new OnClickListener(){
			    public void onClick(DialogInterface dialog, int arg1) {
			      // Do nothing
			    }
			  });
			  
			  ad.setCancelable(true);
			  ad.setOnCancelListener(new OnCancelListener() {
          public void onCancel(DialogInterface dialog) {
			      // do nothing
			    }
			  }); 
			  ad.show();
			
        return true;
			}
	  });
	  
  	// Activity with Dialog Theme
	  menu.add(0, 0, Menu.NONE, "Dialog Themed Activity").setOnMenuItemClickListener(new OnMenuItemClickListener() {		  
			public boolean onMenuItemClick(MenuItem menuItem) {
			  Intent i = new Intent(getApplicationContext(), MyDialog.class);
			  startActivityForResult(i, 0);
			  return true;
		  }
		});
	  
		// Launch a managed Dialog
	  menu.add(0, 0, Menu.NONE, "Managed Dialog").setOnMenuItemClickListener(new OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem menuItem) {
			  showDialog(TIME_DIALOG);
			  return true;			  
		  }
		});
	  
    // Passing Intent to other Activities
	  menu.add(0, 0, Menu.NONE, "Pass Through Intent").setOnMenuItemClickListener(new OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem menuItem) {
        Intent intent = getIntent();
        if (intent != null)
        {
          String action = intent.getAction();
          Uri data = intent.getData();
          startNextMatchingActivity(intent);
        } 
        return true;        
      }
    });
	  
	  // Finished creating menu.
    return true;
	}

  /** ---- Dynamic Dialog Box Creation ---- */
    
	@Override
	public Dialog onCreateDialog(int id) {
	  switch(id) {
	    case (TIME_DIALOG) :
	      // Create a new Alert Dialog that displays the current time.
	      AlertDialog.Builder timeDialog = new AlertDialog.Builder(this);
	      timeDialog.setTitle("The Current Time Is…");
	      timeDialog.setMessage("Now");
	      return timeDialog.create();
	  }
	  return null;
	}

	@Override
	public void onPrepareDialog(int id, Dialog dialog) {
	  switch(id) {
	    case (TIME_DIALOG) :
	      // Update the current time in the time dialog each time
	      // it's displayed.
	      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	      Date currentTime = new Date(java.lang.System.currentTimeMillis());
	      String dateString = sdf.format(currentTime);
	      
	      AlertDialog timeDialog = (AlertDialog)dialog;
	      timeDialog.setMessage(dateString);
	      break;
	  }
	}
  
  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);

     Intent intent = new Intent();
     intent.setData(Contacts.CONTENT_URI);
     intent.addCategory(Intent.CATEGORY_SELECTED_ALTERNATIVE);
     
     menu.addIntentOptions(1, 100, Menu.NONE, getComponentName(), null, intent, Menu.NONE, null);
     
    return true;
  }

  private static String IS_INPUT_CORRECT = "INPUT_CORRECT";
  private static String SELECTED_PISTOL = "SELECTED_PISTOL";
  
  @Override 
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);
   
    switch(requestCode) {
      case (SHOW_SUBACTIVITY) :
        if (resultCode == Activity.RESULT_OK) {
          Uri horse = data.getData();
      	  boolean inputCorrect = data.getBooleanExtra(IS_INPUT_CORRECT, false);
      	  String selectedPistol = data.getStringExtra(SELECTED_PISTOL);
      	  
          Toast.makeText(this, "Pistol Selected: " + selectedPistol, Toast.LENGTH_SHORT).show();        	    
        }
        break;        
        
      case (PICK_CONTACT_SUBACTIVITY) :
        if (resultCode == Activity.RESULT_OK) {
      	Toast.makeText(this, "CONTACT SELECTED", Toast.LENGTH_SHORT).show();
          // TODO Handle contact selection.
        }
        break;       
    }
  }
    
  /** Example of a Broadcast Receiver registered 
   * within an Application Activity
   */
  public static class InternalLifeformDetectedBroadcastReceiver extends BroadcastReceiver {
  
  	public static final String BURN_IT_WITH_FIRE = "com.paad.alien.action.BURN_IT_WITH_FIRE";
  
    @Override
    public void onReceive(Context context, Intent intent) {
      // Get the life-form details from the intent.
      Uri data = intent.getData();
      String type = intent.getStringExtra("type");
      double lat = intent.getDoubleExtra("latitude", 0);
      double lng = intent.getDoubleExtra("longitude", 0); 
      Location loc = new Location("gps");
      loc.setLatitude(lat);
      loc.setLongitude(lng);
  
      if (type.equals("alien")) {
        Toast.makeText(context, "Activity Registered Alien Detector", Toast.LENGTH_LONG).show();
        Intent startIntent = new Intent(BURN_IT_WITH_FIRE, data);
        startIntent.putExtra("latitude", lat);
        startIntent.putExtra("longitude", lng);
        
        context.startActivity(startIntent);
      }
    }
  }

}