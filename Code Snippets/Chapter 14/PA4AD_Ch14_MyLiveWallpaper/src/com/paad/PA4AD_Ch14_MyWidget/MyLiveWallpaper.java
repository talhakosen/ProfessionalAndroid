package com.paad.PA4AD_Ch14_MyWidget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.LiveFolders;

public class MyLiveWallpaper extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Check to confirm this Activity was launched as part
    // of a request to add a new Live Folder to the home screen
    String action = getIntent().getAction();
    if (LiveFolders.ACTION_CREATE_LIVE_FOLDER.equals(action)) {
      Intent intent = new Intent(); 

      // Set the URI of the Content Provider that will supply the
      // data to display. The appropriate projection must already
      // be applied to the returned data.
      intent.setData(MyContentProvider.LIVE_FOLDER_URI);

      // Set the display mode to a list.
      intent.putExtra(LiveFolders.EXTRA_LIVE_FOLDER_DISPLAY_MODE,
                      LiveFolders.DISPLAY_MODE_LIST);

      // Indicate the icon to be used to represent the Live Folder 
      // shortcut on the home screen.
      intent.putExtra(LiveFolders.EXTRA_LIVE_FOLDER_ICON,
                      Intent.ShortcutIconResource.fromContext(this,
                                                              R.drawable.icon));

      // Provide the name to be used to represent the Live Folder on 
      // the home screen. 
      intent.putExtra(LiveFolders.EXTRA_LIVE_FOLDER_NAME, "Earthquakes");
      
      // Specify a base Intent that will request the responding Activity
      // View the selected item.
      intent.putExtra(LiveFolders.EXTRA_LIVE_FOLDER_BASE_INTENT,
                      new Intent(Intent.ACTION_VIEW,
                                 MyContentProvider.CONTENT_URI));


      // Return the Live Folder Intent as a result.
      setResult(RESULT_OK, intent);
    }
    else
      setResult(RESULT_CANCELED);
    
    finish();
  }
  
}