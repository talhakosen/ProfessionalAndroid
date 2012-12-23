package com.paad.config_changes;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public class MyActivity extends Activity {
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
  }
  
  /**
   * Listing 3-6: Handling configuration changes in code
   */
  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig); 

    // [ ... Update any UI based on resource values ... ]

    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      // [ ... React to different orientation ... ]
    }

    if (newConfig.keyboardHidden == Configuration.KEYBOARDHIDDEN_NO) {
      // [ ... React to changed keyboard visibility ... ]
    }
  }
}