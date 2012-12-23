/**
 * Code snippets used to demonstrate the Geolocator and 
 * Location-Based Services.
 */

package com.paad.chapter7;


import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.Toast;

public class MyActivity extends Activity {
  
  private static String TREASURE_PROXIMITY_ALERT = "com.paad.treasurealert";
  
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.main);
  
    // ---- Using the Location Providers
        
    LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    
    boolean enabledOnly = true;
    List<String> providers = locationManager.getProviders(enabledOnly);  
    
    // Finding Location Providers using Criteria
    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    
    Criteria criteria = new Criteria();
    criteria.setAccuracy(10);
    criteria.setAltitudeRequired(false);
    criteria.setBearingRequired(false);
    criteria.setCostAllowed(true);
    criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
    criteria.setSpeedRequired(false);
    
    String bestProvider = locationManager.getBestProvider(criteria, true);
    List<String> matchingProviders = locationManager.getProviders(criteria, false);

    // Requesting location updates using a Location Listener

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
  
      public void onStatusChanged(String provider, int status, Bundle extras){
        // Update application if provider hardware status changed.
      }
    };

    int timeDelta = 2000; // milliseconds
    int distance = 2; // meters

    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeDelta, distance, myLocationListener);
    locationManager.removeUpdates(myLocationListener);
  
    // ---- Geocoders

    // Reverse Geocoding
    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    double latitude = location.getLatitude();
    double longitude = location.getLongitude();

    List<Address> addresses = null;

    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
    try {
      addresses = geocoder.getFromLocation(latitude, longitude, 10);
    } catch (IOException e) {}

    Toast.makeText(this, addresses.get(0).getCountryName(), Toast.LENGTH_LONG).show();

    // Forward Geocoding
    String streetAddress = "160 Riverside Drive, New York, New York";

    List<Address> locations = null;
    Geocoder fwdGeocoder = new Geocoder(this, Locale.US);
    try {
      locations = fwdGeocoder.getFromLocationName(streetAddress, 10);
    } catch (IOException e) {}
    
    String latLngString = addresses.get(0).getLatitude() + ", " + addresses.get(0).getLongitude();
    Toast.makeText(this, latLngString , Toast.LENGTH_LONG).show();
    
    // Proximity Alert
    setProximityAlert();
  }
    
  /** Configure a proximity alert */
  private void setProximityAlert() {
    LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

    double lat = 73.147536;
    double lng = 0.510638;
    float radius = 100f; //meters
    long expiration = -1; //do not expire
   
    Intent intent = new Intent(TREASURE_PROXIMITY_ALERT);
    PendingIntent proximityIntent = PendingIntent.getBroadcast(getApplicationContext(), -1, intent, 0);
    locationManager.addProximityAlert(lat, lng, radius, expiration, proximityIntent);
    
    IntentFilter filter = new IntentFilter(TREASURE_PROXIMITY_ALERT);
    registerReceiver(new ProximityIntentReceiver(), filter);
  }

  /** Proximity Alert Broadcast Receiver */
  public class ProximityIntentReceiver extends BroadcastReceiver {       
	  @Override
	  public void onReceive (Context context, Intent intent) {
	    String key = LocationManager.KEY_PROXIMITY_ENTERING;

	    Boolean entering = intent.getBooleanExtra(key, false);
	    Toast.makeText(MyActivity.this, "Treasure: " + entering, Toast.LENGTH_LONG).show();
	    //[ ... perform proximity alert actions ... ]
	  }
  }
}   