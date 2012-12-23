/**
 * Listing 13-7: Design pattern for switching Location Providers when a better alternative becomes available
 */
package com.paad.location;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class DynamicProvidersActivity extends Activity {
  private LocationManager locationManager;
  private final Criteria criteria = new Criteria();
  private static int minUpdateTime = 0;
  private static int minUpdateDistance = 0;
  
  private static final String TAG = "DYNAMIC_LOCATION_PROVIDER";
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    // Get a reference to the Location Manager
    String svcName = Context.LOCATION_SERVICE;
    locationManager = (LocationManager)getSystemService(svcName);
    
    // Specify Location Provider criteria
    criteria.setAccuracy(Criteria.ACCURACY_FINE);
    criteria.setPowerRequirement(Criteria.POWER_LOW);
    criteria.setAltitudeRequired(true);
    criteria.setBearingRequired(true);
    criteria.setSpeedRequired(true);
    criteria.setCostAllowed(true);
    
    // Only for Android 3.0 and above
    criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
    criteria.setVerticalAccuracy(Criteria.ACCURACY_MEDIUM);
    criteria.setBearingAccuracy(Criteria.ACCURACY_LOW);
    criteria.setSpeedAccuracy(Criteria.ACCURACY_LOW); 
    // End of Android 3.0 and above only        
  }
  
  @Override
  protected void onPause() {
    unregisterAllListeners();
    super.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    registerListener();
  }
    
  private void registerListener() {
    unregisterAllListeners();
    String bestProvider = 
      locationManager.getBestProvider(criteria, false);
    String bestAvailableProvider = 
      locationManager.getBestProvider(criteria, true);
    
    Log.d(TAG, bestProvider + " / " + bestAvailableProvider);
  
    if (bestProvider == null)
      Log.d(TAG, "No Location Providers exist on device.");
    else if (bestProvider.equals(bestAvailableProvider))
      locationManager.requestLocationUpdates(bestAvailableProvider, 
        minUpdateTime, minUpdateDistance,
        bestAvailableProviderListener);
    else {
      locationManager.requestLocationUpdates(bestProvider, 
        minUpdateTime, minUpdateDistance, bestProviderListener);
      
      if (bestAvailableProvider != null)
        locationManager.requestLocationUpdates(bestAvailableProvider,
          minUpdateTime, minUpdateDistance, 
          bestAvailableProviderListener);
      else {
        List<String> allProviders = locationManager.getAllProviders();
        for (String provider : allProviders)
          locationManager.requestLocationUpdates(provider, 0, 0,
            bestProviderListener);
        Log.d(TAG, "No Location Providers currently available.");
      }
    }
  }
  
  private void unregisterAllListeners() {
    locationManager.removeUpdates(bestProviderListener);
    locationManager.removeUpdates(bestAvailableProviderListener);
  }
  
  private void reactToLocationChange(Location location) {
    // TODO [ React to location change ]
  } 
  
  private LocationListener bestProviderListener 
    = new LocationListener() {
    
    public void onLocationChanged(Location location) {
      reactToLocationChange(location);
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
      registerListener();
    }

    public void onStatusChanged(String provider,
                                int status, Bundle extras) {}
  };
  
  private LocationListener bestAvailableProviderListener = 
    new LocationListener() {
    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
      registerListener();
    }

    public void onLocationChanged(Location location) {
      reactToLocationChange(location);
    }
    
    public void onStatusChanged(String provider, 
                                int status, Bundle extras) {}
  };
}
