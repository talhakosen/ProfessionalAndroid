package com.paad.broadcastintents;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class MyActivity extends Activity {
  /**
   * Listing 5-12: Registering and unregistering a Broadcast Receiver in code 
   */
  private IntentFilter filter = 
      new IntentFilter(LifeformDetectedReceiver.NEW_LIFEFORM);

  private LifeformDetectedReceiver receiver = 
    new LifeformDetectedReceiver();

  @Override
  public synchronized void onResume() {
    super.onResume();

    // Register the broadcast receiver.
    registerReceiver(receiver, filter); 
  }

  @Override
  public synchronized void onPause() {
    // Unregister the receiver
    unregisterReceiver(receiver);  

    super.onPause();
  }
  
  //
  private void detectedLifeform(String detectedLifeform, double currentLongitude, double currentLatitude) {
    Intent intent = new Intent(LifeformDetectedReceiver.NEW_LIFEFORM);
    intent.putExtra(LifeformDetectedReceiver.EXTRA_LIFEFORM_NAME,
                    detectedLifeform);
    intent.putExtra(LifeformDetectedReceiver.EXTRA_LONGITUDE,
                    currentLongitude);
    intent.putExtra(LifeformDetectedReceiver.EXTRA_LATITUDE,
                    currentLatitude);

    sendBroadcast(intent);
  }
  
  //
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
  }

}