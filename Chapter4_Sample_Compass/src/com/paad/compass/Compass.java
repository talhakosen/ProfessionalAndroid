package com.paad.compass;

import android.app.Activity;
import android.os.Bundle;

public class Compass extends Activity {

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);      
        
    setContentView(R.layout.main);
    CompassView cv = (CompassView)this.findViewById(R.id.compassView);
    cv.setBearing(45);
  }
}