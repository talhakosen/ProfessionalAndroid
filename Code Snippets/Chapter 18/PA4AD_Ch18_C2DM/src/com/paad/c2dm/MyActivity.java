package com.paad.c2dm;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

public class MyActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
  }
  
  private void registerWithC2DMServer() {
    /**
     * Listing 18-3: Registering an application instance with the C2DM server
     */
    Intent registrationIntent = 
      new Intent("com.google.android.c2dm.intent.REGISTER");

    registrationIntent.putExtra("app",   
      PendingIntent.getBroadcast(this, 0, new Intent(), 0));

    registrationIntent.putExtra("sender",
                                "myC2DMaccount@gmail.com");

    startService(registrationIntent);
  }
}