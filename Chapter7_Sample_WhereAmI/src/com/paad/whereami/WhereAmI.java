package com.paad.whereami;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class WhereAmI extends Activity {
 
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.main);

    LocationManager locationManager;
    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    updateWithNewLocation(location);
  }
  
  /** Update UI with a new location */
  private void updateWithNewLocation(Location location) {
	  TextView myLocationText = (TextView)findViewById(R.id.myLocationText);
	  
	  String latLongString;
	   
    if (location != null) {
	    double lat = location.getLatitude();
	    double lng = location.getLongitude();
	    latLongString = "Lat:" + lat + "\nLong:" + lng;
	  } else {
	    latLongString = "No location found"; 
	  }
    
	  myLocationText.setText("Your Current Position is:\n" + latLongString);  
	}

}
