package com.paad.whereami;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class WhereAmI extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    LocationManager locationManager;
    String svcName = Context.LOCATION_SERVICE;
    locationManager = (LocationManager)getSystemService(svcName);

    Criteria criteria = new Criteria();
    criteria.setAccuracy(Criteria.ACCURACY_FINE);
    criteria.setPowerRequirement(Criteria.POWER_LOW);
    criteria.setAltitudeRequired(false);
    criteria.setBearingRequired(false);
    criteria.setSpeedRequired(false);
    criteria.setCostAllowed(true);
    String provider = locationManager.getBestProvider(criteria, true);
    
    Location l = locationManager.getLastKnownLocation(provider);

    updateWithNewLocation(l);
    
    locationManager.requestLocationUpdates(provider, 2000, 10,
                                           locationListener);
  }

  private void updateWithNewLocation(Location location) {
    TextView myLocationText;
    myLocationText = (TextView)findViewById(R.id.myLocationText);
      
    String latLongString = "No location found";
    if (location != null) {
      double lat = location.getLatitude();
      double lng = location.getLongitude();
      latLongString = "Lat:" + lat + "\nLong:" + lng;
    }
      
    myLocationText.setText("Your Current Position is:\n" +
                           latLongString);
  }
  
  private final LocationListener locationListener = new LocationListener() {
    public void onLocationChanged(Location location) {
      updateWithNewLocation(location);
    }

    public void onProviderDisabled(String provider) {}
    public void onProviderEnabled(String provider) {}
    public void onStatusChanged(String provider, int status, 
                                Bundle extras) {}
  };

}