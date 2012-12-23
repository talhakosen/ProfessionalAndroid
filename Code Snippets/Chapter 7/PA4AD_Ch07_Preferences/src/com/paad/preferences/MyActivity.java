package com.paad.preferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class MyActivity extends Activity {
  
  private static final int SHOW_PREFERENCES = 0;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    /**
     * Listing 7-3: Runtime selection of pre- or post-Honeycomb Preference Activities
     */
    Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?   
        MyPreferenceActivity.class : MyFragmentPreferenceActivity.class;

      Intent i = new Intent(this, c);
      startActivityForResult(i, SHOW_PREFERENCES);
  }
}