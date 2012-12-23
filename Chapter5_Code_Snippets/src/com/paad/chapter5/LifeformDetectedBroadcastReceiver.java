package com.paad.chapter5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.widget.Toast;

/** Example of a Broadcast Receiver registered 
 * in the application Manifest
 */
public class LifeformDetectedBroadcastReceiver extends BroadcastReceiver {

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
      Toast.makeText(context, "Manifest Registered Lifeform Detector", Toast.LENGTH_LONG).show();

      // Construct a new Intent to react to Alien detection.
      Intent startIntent = new Intent(BURN_IT_WITH_FIRE, data);
      startIntent.putExtra("latitude", lat);
      startIntent.putExtra("longitude", lng);
      
      // Start a new Activity to execute the Intent.
      context.startActivity(startIntent);
    }
  }
}