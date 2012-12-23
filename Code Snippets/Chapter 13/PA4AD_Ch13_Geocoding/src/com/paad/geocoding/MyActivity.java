package com.paad.geocoding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class MyActivity extends Activity {
  private static final String TAG = "GEOCODING";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    Location location = new Location("Faked");
    location.setLatitude(115.853379);
    location.setLongitude(-31.951749);
    reverseGeocode(location);
    
    forwardGeocode();
  }
  
  /**
   * Listing 13-10: Reverse-geocoding a given location
   */
  private void reverseGeocode(Location location) {

    double latitude = location.getLatitude();
    double longitude = location.getLongitude();
    List<Address> addresses = null;

    Geocoder gc = new Geocoder(this, Locale.getDefault());
    try {
      addresses = gc.getFromLocation(latitude, longitude, 10);
    } catch (IOException e) {
      Log.e(TAG, "IO Exception", e);
    }
  }
  
  private void forwardGeocode() {
    /**
     * Listing 13-11: Geocoding an address
     */
    Geocoder fwdGeocoder = new Geocoder(this, Locale.US);
    String streetAddress = "160 Riverside Drive, New York, New York";

    List<Address> locations = null;
    try {
      locations = fwdGeocoder.getFromLocationName(streetAddress, 5);
    } catch (IOException e) {
      Log.e(TAG, "IO Exception", e);
    }

  }

}