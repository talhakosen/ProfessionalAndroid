package com.paad.location;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyActivity extends Activity {
  
  private static final boolean EMULATOR_TESTING = false;
  
  LocationListener myLocationListener;
  PendingIntent pendingIntent;
  ProximityIntentReceiver proximityIntentReceiver;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    /**
     * Listing 13-1: Accessing the Location Manager 
     */
    String serviceString = Context.LOCATION_SERVICE;
    LocationManager locationManager;
    locationManager = (LocationManager)getSystemService(serviceString);

    if (EMULATOR_TESTING) {
      /**
       * Listing 13-2: Enabling the GPS provider on the Emulator
       */
      locationManager.requestLocationUpdates(
        LocationManager.GPS_PROVIDER, 0, 0, 
        new LocationListener() {
          public void onLocationChanged(Location location) {}
          public void onProviderDisabled(String provider) {}
          public void onProviderEnabled(String provider) {}
          public void onStatusChanged(String provider, int status, 
                                      Bundle extras) {}
        }
      );
    }
     
    /**
     * Listing 13-3: Specifying Location Provider
     */
    Criteria criteria = new Criteria();
    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
    criteria.setPowerRequirement(Criteria.POWER_LOW);
    criteria.setAltitudeRequired(false);
    criteria.setBearingRequired(false);
    criteria.setSpeedRequired(false);
    criteria.setCostAllowed(true);
    
    //
    registerLocationUpdates(locationManager);
    registerPendingIntentLoctionListener(locationManager);
    setProximityAlert();
    IntentFilter filter = new IntentFilter(TREASURE_PROXIMITY_ALERT);
    proximityIntentReceiver = new ProximityIntentReceiver();
    registerReceiver(proximityIntentReceiver, filter);
  }
  
  private void registerLocationUpdates(LocationManager locationManager) {
    /**
     * Listing 13-4: Requesting location updates Using a Location Listener 
     */
    String provider = LocationManager.GPS_PROVIDER;

    int t = 5000;     // milliseconds
    int distance = 5; // meters

    LocationListener myLocationListener = new LocationListener() {

      public void onLocationChanged(Location location) {
        // Update application based on new location.
      }

      public void onProviderDisabled(String provider){
        // Update application if provider disabled.
      }

      public void onProviderEnabled(String provider){
        // Update application if provider enabled.
      }

      public void onStatusChanged(String provider, int status,
                                  Bundle extras){
        // Update application if provider hardware status changed.
      }
    };

    locationManager.requestLocationUpdates(provider, t, distance,
                                           myLocationListener);
    
    //
    this.myLocationListener = myLocationListener;
  }
  
  private void registerPendingIntentLoctionListener(LocationManager locationManager) {
    /**
     * Listing 13-5: Requesting location updates using a Pending Intent 
     */
    String provider = LocationManager.GPS_PROVIDER;

    int t = 5000;     // milliseconds
    int distance = 5; // meters

    final int locationUpdateRC = 0;  
    final String locationUpdateAction 
      = "com.paad.LOCATION_UPDATE_RECEIVED";
    int flags = PendingIntent.FLAG_UPDATE_CURRENT;

    Intent intent = new Intent(locationUpdateAction);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 
      locationUpdateRC, intent, flags);

    locationManager.requestLocationUpdates(provider, t, 
                                           distance, pendingIntent);
    
    // Register Receiver
    this.pendingIntent = pendingIntent;
    registerReceiver(new MyLocationUpdateReceiver(), new IntentFilter(locationUpdateAction));
  }

  @Override
  protected void onPause() {
    String serviceString = Context.LOCATION_SERVICE;
    LocationManager locationManager;
    locationManager = (LocationManager)getSystemService(serviceString);
    
    locationManager.removeUpdates(myLocationListener);
    locationManager.removeUpdates(pendingIntent);
    
    unregisterReceiver(proximityIntentReceiver);
    PendingIntent proximityIntent = PendingIntent.getBroadcast(this, -1,
        new Intent(TREASURE_PROXIMITY_ALERT),
        0);
    locationManager.removeProximityAlert(proximityIntent);

    super.onPause();
  }

  /**
   * Listing 13-8: Setting a proximity alert
   */
  private static final String TREASURE_PROXIMITY_ALERT = "com.paad.treasurealert";

  private void setProximityAlert() {
    String locService = Context.LOCATION_SERVICE;
    LocationManager locationManager;
    locationManager = (LocationManager)getSystemService(locService);

    double lat = 73.147536;
    double lng = 0.510638;
    float radius = 100f; // meters
    long expiration = -1; // do not expire

    Intent intent = new Intent(TREASURE_PROXIMITY_ALERT);
    PendingIntent proximityIntent = PendingIntent.getBroadcast(this, -1,
                                                               intent,
                                                               0);
    locationManager.addProximityAlert(lat, lng, radius,
                                      expiration,
                                      proximityIntent);
  }
  
}