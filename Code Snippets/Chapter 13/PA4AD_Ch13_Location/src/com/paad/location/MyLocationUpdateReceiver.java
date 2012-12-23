package com.paad.location;

/**
 * Listing 13-6: Receiving location updates using a Broadcast Receiver 
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

public class MyLocationUpdateReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    String key = LocationManager.KEY_LOCATION_CHANGED;
    Location location = (Location)intent.getExtras().get(key);
     // TODO [... Do something with the new location ...]
  }

}
